package com.practies.musicapp.service

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.practies.musicapp.*

import com.practies.musicapp.model.Music
import com.practies.musicapp.interfaces.OnSongComplete
import com.practies.musicapp.model.lastPlayedSongId
import com.practies.musicapp.model.musicServices
import com.practies.musicapp.musicDatabase.MusicDao
import com.practies.musicapp.musicDatabase.MusicDatabase
import com.practies.musicapp.notifications.ApplicationClass
import com.practies.musicapp.notifications.NotificationReceiver
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import java.lang.Runnable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


//,MediaPlayer.OnPreparedListener , mediaPlayer= MediaPlayer(),MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener
class MusicServices :Service(),MediaPlayer.OnCompletionListener  {






    var lastplayedSong:Music?=null

    var repeat:Boolean=false
    var currentIndex = 0
    val intervell = 1000
    var isPlaying = false
    var isPause=false
    lateinit var recentSong:Music
    companion object{     var songCurrentTitle:String =""}
     lateinit var  favMusicDa:MusicDao
    lateinit var seekBar: SeekBar
    lateinit var onSongComplete: OnSongComplete
   lateinit  var mediaPlayer: MediaPlayer
    lateinit var mediaSession: MediaSessionCompat
    lateinit var receiver: NotificationReceiver
    private var mybinder = Mybinder()
    var musiclistSe = arrayListOf<Music>()
    var tempListSe= arrayListOf<Music>()
    var favoritelistSe= arrayListOf<Music>()
    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")


        return mybinder
    }

    inner class Mybinder : Binder() {

        fun currentService(): MusicServices {

            return this@MusicServices
        }
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       //  Notification bar play functions

        Log.i("MSG",intent?.action?: "null")

          when(intent?.action){

           ApplicationClass.PREVIOUS->{    nextPreviousSong(false) }

           ApplicationClass.PLAY ->{           //play or pause song

               if (mediaPlayer.isPlaying){ playPauseMusic(false)

                   val lastplayedSong=musiclistSe[currentIndex]

                   Log.i("LastSong in serviese",lastplayedSong.toString())
                  // Toast.makeText(baseContext, lastplayedSong.title,Toast.LENGTH_LONG).show()
                   val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE).edit()
                   editor.clear()
                   editor.apply()
                   val jSonString= GsonBuilder().create().toJson(lastplayedSong)
                   editor.putString("LastPlayedSong",jSonString,)
                   editor.apply()




               }
               else{ playPauseMusic(true) }
           }

           ApplicationClass.NEXT ->{
               //next song
           nextPreviousSong(true)
           }
           ApplicationClass.EXit ->{        //Exit app   and notification




               mediaPlayer.pause()
               mediaPlayer.stop()
               musicServices=null
               stopForeground(true)
               exitProcess(1)

           }
          }



        return super.onStartCommand(intent, flags, startId)

    }


       //song complete
    fun setListener( onSongComplete: OnSongComplete){
        this.onSongComplete=onSongComplete
    }


    override fun onCreate() {
        super.onCreate()


        val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE)
        val jSonString = editor.getString("LastPlayedSong",null)
        val typeToken =object : TypeToken<Music>(){}.type

        if (jSonString != null){
            recentSong = GsonBuilder().create().fromJson(jSonString,typeToken)
            Log.i("RecentSong:::",recentSong.title)
        }




        favMusicDa=MusicDatabase.getDatabase(this).musicDao()
        GlobalScope.launch (Dispatchers.IO){
            playList =favMusicDa.getAllPlayListName() as ArrayList<String>
            Log.i("DB", playList.toString())
        }
        // mediaPlayer = MediaPlayer()
        seekBar = SeekBar(this)

        initMediaPlayer()






        //////to  access  the database//*******************************************************


    }



    /////////////////////To  check current song is favorite or not
//    fun faveChecker():Boolean {
//        var songExist = false
//        val currentSong = musiclistSe[currentIndex]
//        GlobalScope.launch(Dispatchers.IO) {
//            songExist =favMusicDa.checkSongExist(currentSong.id, favorite)
//        }
//        return songExist
//    }
    /////////////////////////
//    fun removeSongFromList(list:ArrayList<Music>,songPosition: Int,context: Context) {
//        val alertDialog = AlertDialog.Builder(context)
//        alertDialog.setTitle("Delete Song")
//        alertDialog.setMessage("Are you sure ,want to delete")
//        alertDialog.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
//            dialogInterface.cancel()
//        }
//        alertDialog.setPositiveButton("Yes") { _: DialogInterface, i: Int ->
//
//            val song = list[songPosition]
//            GlobalScope.launch(Dispatchers.IO) { favMusicDa.deleteSong(song) }
//        }
//    }



///////////To  remove or add the favorite song

    private fun favoriteSongAddOrRemove(){
        var songExist:Boolean
        val currentSong=musiclistSe[currentIndex]
               GlobalScope.launch (Dispatchers.IO){   songExist= favMusicDa.checkSongExist(currentSong.id, favorite)

                   withContext(Dispatchers.IO){
                        if (!songExist){
                            currentSong.play_list_name= favorite
                            currentSong.timeStamp=System.currentTimeMillis().toString()+currentSong.id
                            val  favoriteMusic= Music(
                                currentSong.timeStamp,
                                currentSong.id,currentSong.title,currentSong.album,currentSong.artist,
                                currentSong.duration,currentSong.path,currentSong.artUri,currentSong.play_list_name,
                            )
                            favMusicDa.addSong(favoriteMusic)
                            Log.i("Favourite", "Song added")
                        }else{
                            favMusicDa.deleteSong(currentSong)
                            Log.i("Favorite","song removed from fav")
                   }
               }
        }

    }
    //to play from the lastPlayed song
     fun setInitialView(list:ArrayList<Music>){

            musiclistSe=list


        try {
            //   mediaPlayer=MediaPlayer()
            EventBus.getDefault().post(recentSong)
            mediaPlayer.reset()
            mediaPlayer.setDataSource(recentSong.path)
            Log.i("MusicInitial", " music list is ok")
            mediaPlayer.prepare()


//            lastplayedSong=musiclistSe[currentIndex]
//            lastPlayedSongId=musiclistSe[currentIndex].id
//            showNotification(R.drawable.pause_bt_circle)


        } catch (e: Exception) {
            return
        }


     }



    fun setSongList(songList:ArrayList<Music>, songPosition:Int){
          musiclistSe=songList
          currentIndex=songPosition

         // initMediaPlayer()
          playSong()


      }





    fun playSong() {


        try {
            //   mediaPlayer=MediaPlayer()
            EventBus.getDefault().post(musiclistSe[currentIndex])
            mediaPlayer.reset()
            mediaPlayer.setDataSource(musiclistSe[currentIndex].path)
            Log.i("Music", " music list is ok")
            mediaPlayer.prepare()
            mediaPlayer.start()
            isPlaying = true
              lastplayedSong=musiclistSe[currentIndex]
             lastPlayedSongId=musiclistSe[currentIndex].id
           showNotification(R.drawable.pause_bt_circle)


        } catch (e: Exception) {
            return
        }


    }


    fun initMediaPlayer() {
        Log.i("MSG", "init player invoked")
          mediaPlayer= MediaPlayer()
        mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnCompletionListener(this)

    }

    override fun onCompletion(mp: MediaPlayer?) {

      onSongComplete.onSongComplete()
        Log.i("Tag","complete listener called")}



    private val progressRunner = object : Runnable {
        override fun run() {
            seekBar.progress = mediaPlayer.currentPosition
            if (mediaPlayer.isPlaying) {
                seekBar.postDelayed(this, intervell.toLong())
            }
        }
    }


    fun playPauseMusic(isPlay: Boolean) {
        if (isPlay) {
//  for Resume the song
            mediaPlayer.start()
            progressRunner.run()
            isPlaying= true
              showNotification(R.drawable.pause_bt_circle)
            //playSong()

        } else {
            mediaPlayer.pause()
            lastPlayedSongId= musiclistSe[currentIndex].id
            seekBar.removeCallbacks(progressRunner)
            showNotification(R.drawable.play_bt_circle)
            isPlaying = false
        }
    }


    fun nextPreviousSong(increment: Boolean) {
        if (increment) {
            setSongPosition(increment = true)
            mediaPlayer.reset()
            playSong()

        } else {
            setSongPosition(increment = false)
            mediaPlayer.reset()
            playSong()
        }

    }

    fun setSongPosition(increment: Boolean) {
        if (increment) {

            if (musiclistSe.size - 1 == currentIndex) {
                currentIndex = 0
            } else
                ++currentIndex
        } else {

            if (currentIndex == 0) {
                currentIndex = musiclistSe.size - 1
            } else
                --currentIndex
        }

    }


    fun showNotification(playPause:Int){

        val prevIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
          val prevPendingIntent=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.EXit)
        val exitPendingIntent=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)

          val imgArt= getImageArt(musiclistSe[currentIndex].path)

      val image=  if (imgArt != null){ BitmapFactory.decodeByteArray(imgArt,0,imgArt.size) }else{
            BitmapFactory.decodeResource(resources,R.drawable.headphone)
        }

        val notification = NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentTitle(musiclistSe[currentIndex].title)
            .setContentText(musiclistSe[currentIndex].artist)
            .setSmallIcon(R.mipmap.music_player_icon)
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setColor(3900)
            .setOnlyAlertOnce(true)

            .addAction(R.drawable.previous_button,"Previous",prevPendingIntent)


            .addAction(playPause,"PlayPause",playPendingIntent)
            .addAction(R.drawable.next_button,"Next",nextPendingIntent)
            .addAction(R.drawable.exit_icon,"Exit",exitPendingIntent)
            .build()
        startForeground(11,notification)

    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.i("LastSong in serviese",lastplayedSong.toString())
//        Toast.makeText(baseContext,lastplayedSong?.title ,Toast.LENGTH_LONG).show()
//        val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE).edit()
//        editor.clear()
//        editor.apply()
//        val jSonString= GsonBuilder().create().toJson(lastplayedSong)
//        editor.putString("LastPlayedSong",jSonString,)
//        editor.apply()


//        val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE).edit()
//         val jSonString= GsonBuilder().create().toJson(lastplayedSong)
//           editor.putString("LastPlayedSong",jSonString,)
//             editor.apply()
    }


}





















































