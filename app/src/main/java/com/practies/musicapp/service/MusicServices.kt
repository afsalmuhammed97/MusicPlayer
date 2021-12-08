package com.practies.musicapp.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
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
import com.practies.musicapp.Music
import com.practies.musicapp.formatDuration
import com.practies.musicapp.interfaces.OnSongComplete
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


//,MediaPlayer.OnPreparedListener , mediaPlayer= MediaPlayer(),MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener
class MusicServices :Service(),MediaPlayer.OnCompletionListener  {
    var currentIndex = 0
    var songPosionSe = 0
    val intervell = 1000
    var isPlaying = false
    lateinit var startPoint:TextView
    lateinit var entPoint:TextView
    lateinit var seekBar: SeekBar
    lateinit var onSongComplete: OnSongComplete
    lateinit var mediaPlayer: MediaPlayer


    lateinit var mediaSession: MediaSessionCompat
    private var mybinder = Mybinder()
    var musiclistSe =
        arrayListOf<Music>()              //mutableListOf<Music>() //arrayListOf<Music>()

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return mybinder
    }

    inner class Mybinder : Binder() {

        fun currentService(): MusicServices {

            return this@MusicServices
        }
    }
       //song complete
    fun setListener( onSongComplete: OnSongComplete){
        this.onSongComplete=onSongComplete
    }
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
      seekBar = SeekBar(this)

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



//        mp!!.reset()
//        mp.setDataSource(musiclistSe[ ++currentIndex].path)
//        mp.prepare()
//        mp.start()
//        mp?.stop()
//        mp?.release()
//
//        mp?.setDataSource(musiclistSe[ ++currentIndex].path)
//        mp?.prepare()
//        mp?.start()




  //  override fun onPrepared(mp: MediaPlayer?) {

//        mp!!.start()
//
//        val duration = mp.duration
//          seekBar.max=duration
//       // seekBar.max = (duration / 1000)
//        seekBar.postDelayed(progressRunner, intervell.toLong())
//        Log.i("Tag","seekbar Runner called") }

    fun setUi( seekBar: SeekBar,start_int:TextView){
      //  this.seekBar=seekBar
       this.startPoint=start_int



//        //  To  seek the seekBar
//                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//
//
//                if (fromUser)mediaPlayer.seekTo(progress*2500)         //seekTo(progress*2500)
//
//                start_int.text= formatDuration(progress.toLong())
//
//
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
//            override fun onStopTrackingTouch(seekBar: SeekBar?) =Unit
//        })


    }
    fun setCurrentPlayerTimeText(ms:Long){
        val dateFormat= SimpleDateFormat("mm:ss", Locale.getDefault())

    }


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
            isPlaying= true//

            //playSong()

        } else {

            mediaPlayer.pause()
            seekBar.removeCallbacks(progressRunner)
            isPlaying = false

        }
    }
    fun onResumeSong(){
        mediaPlayer.start()
        progressRunner.run()
        isPlaying= true
    }

    fun playMusic() {
        playSong()

    }

    fun pauseMusic() {
        mediaPlayer.pause()

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


}

//    fun playSong(isPlay:Boolean){
//
//        if (isPlay){
//            mediaPlayer.pause()
//            isPlaying =false
//        }else
//           // mediaPlayer.start()
//        isPlaying=true
//        playSong()
//    }


//    override fun onPrepared(mp: MediaPlayer?) {
//        mp!!.start()
//        val duration = mp.duration
//        seekBar.max = duration
//        seekBar.postDelayed(progressRunner, interval.toLong())
//        end_tv.text = formatDuration(mp.duration.toLong())
//
//    }
//    private val progressRunner= object :Runnable{
//        override fun run() {
//           seekBar.progress=mediaPlayer.currentPosition
//                if (mediaPlayer.isPlaying){
//                    seekBar.postDelayed(this,interval.toLong())
//                }
//        } }









    // play music
//    private fun playSong(){
//        mediaPlayer.reset()
//        val playSong=songs
//        val currentSongId=playSong.id
//        val trackUris=  ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currentSongId)
//
//        mediaPlayer.setDataSource(applicationContext,trackUris)
//        mediaPlayer.prepareAsync()
//      //  progressRunner.run()
  //  }
















































