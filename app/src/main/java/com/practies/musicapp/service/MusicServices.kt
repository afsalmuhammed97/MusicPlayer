package com.practies.musicapp.service

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.practies.musicapp.Music
import com.practies.musicapp.PlayScreenActivity

import com.practies.musicapp.R
import com.practies.musicapp.database.FavoriteDataBase
import com.practies.musicapp.database.FavoriteDataBase.Companion.getDatabase
import com.practies.musicapp.database.MusicDao
import com.practies.musicapp.formatDuration
import com.practies.musicapp.interfaces.OnSongComplete
import com.practies.musicapp.model.FavoriteMusic
import com.practies.musicapp.notifications.ApplicationClass
import com.practies.musicapp.notifications.NotificationReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


//,MediaPlayer.OnPreparedListener , mediaPlayer= MediaPlayer(),MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener
class MusicServices :Service(),MediaPlayer.OnCompletionListener  {








    var currentIndex = 0
    var songPosionSe = 0
    val intervell = 1000
    var isPlaying = false
    companion object{   //    var isFavorite:Boolean=false
    }
     lateinit var favMusicDao:MusicDao                   // lateinit var musicFavDao: musicDao
    lateinit var seekBar: SeekBar
    lateinit var onSongComplete: OnSongComplete
   lateinit  var mediaPlayer: MediaPlayer
    lateinit var mediaSession: MediaSessionCompat
    private var mybinder = Mybinder()
    var musiclistSe = arrayListOf<Music>()              //mutableListOf<Music>() //arrayListOf<Music>()
     var favoritelistSe:MutableList<FavoriteMusic> =ArrayList()         //arrayListOf<Music>()

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
        Toast.makeText(this,"${intent?.action}",Toast.LENGTH_SHORT).show()
        // Notification bar play functions
          when(intent?.action){

           ApplicationClass.PREVIOUS->{
               //previous song
               Log.i("MSG","previous button clicked")
               //Toast.makeText(baseContext,"preve button",Toast.LENGTH_SHORT).show()
           }
           ApplicationClass.PLAY ->{           //play or pause song
               Toast.makeText(baseContext,"play button",Toast.LENGTH_SHORT).show()
               Log.i("MSG","play button clicked")

               if (mediaPlayer.isPlaying){

                  playPauseMusic(false)
                  // bindingPlayScreen.playPauseButton.setImageResource(R.drawable.play_bt_circle)

               }else{
                  playPauseMusic(true)
               //   PlayScreenActivity.playPauseButton.setImageResource(R.drawable.pause_bt_circle)

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
        //notificationFunctions(notificationMsg)

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
        favMusicDao=getDatabase(this).musicDao()


//        GlobalScope.launch (Dispatchers.IO){ favoritelistSe= favMusicDao.readAllSongs()  }
//              Log.i("Serveice" ,favoritelistSe.toString())

    }
 //   to check the current song is included in favorite list or not
//    fun favoriteChecker(id:String): Int {
//
//       // var favIndex:Int=-1
//         favoritelistSe.forEachIndexed{currentIndex,music ->
//             if (id==music.id){
//               //  isFavorite=true
//                return currentIndex
//             }
//         }
//
//return  -1
//
//
//    }


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

        } catch (e: Exception) {
            return
        }


    }


    fun initMediaPlayer() {
        Log.i("MSG", "init player invoked")
        Toast.makeText(this, "player inited", Toast.LENGTH_SHORT).show()
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


    fun showNotification(){

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





}





















































