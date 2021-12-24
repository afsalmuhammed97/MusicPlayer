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
import com.practies.musicapp.Constants
import com.practies.musicapp.model.Music
import com.practies.musicapp.R
import com.practies.musicapp.favorite
import com.practies.musicapp.interfaces.OnSongComplete
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







    var repeat:Boolean=false
    var currentIndex = 0
    val intervell = 1000
    var isPlaying = false
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



          when(intent?.action){

           ApplicationClass.PREVIOUS->{
               //previous song
               Log.i("MSG","previous button clicked")
               Toast.makeText(baseContext,"preve button",Toast.LENGTH_SHORT).show()
           }
           ApplicationClass.PLAY ->{           //play or pause song
               Toast.makeText(baseContext,"play button",Toast.LENGTH_SHORT).show()
               Log.i("MSG","play button clicked")

               if (mediaPlayer.isPlaying){

                  playPauseMusic(false)

               }else{
                  playPauseMusic(true)

                  }

           }
           ApplicationClass.NEXT ->{          //next song
               Toast.makeText(this,"Next button",Toast.LENGTH_SHORT).show()
               Log.i("MSG","next button clicked")

           }
           ApplicationClass.EXit ->{        //Exit app   and notification
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

        mediaPlayer = MediaPlayer()
      seekBar = SeekBar(this)

        //////to  access  the database//*******************************************************

      favMusicDa=MusicDatabase.getDatabase(this).musicDao()
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




    fun setSongList(songList:ArrayList<Music>, songPosition:Int){
          musiclistSe=songList
          currentIndex=songPosition

          initMediaPlayer()
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
             songCurrentTitle=musiclistSe[currentIndex].title
        } catch (e: Exception) {
            return
        }


    }


    fun initMediaPlayer() {
        Log.i("MSG", "init player invoked")
        //  mediaPlayer= MediaPlayer()
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

            //playSong()

        } else {
            mediaPlayer.pause()
            seekBar.removeCallbacks(progressRunner)
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




        val notification = NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentTitle(musiclistSe[currentIndex].title)
            .setContentText(musiclistSe[currentIndex].artist)
            .setSmallIcon(R.mipmap.music_player_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.headphone))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)

            .addAction(R.drawable.previous_button,"Previous",prevPendingIntent)
            .addAction(R.drawable.pause_bt_circle,"PlayPause",playPendingIntent)
            .addAction(R.drawable.next_button,"Next",nextPendingIntent)
            .addAction(R.drawable.exit_icon,"Exit",exitPendingIntent)
            .build()
        startForeground(11,notification)

    }

//     fun favoriteCheck(id:String):Boolean{
//         isFavorite=false
//         favoritelistSe.contains()
//             if (id ==music.id){
//                 isFavorite=true
//                 return true
//             }
//         }
//         return  false
//     }



}





















































