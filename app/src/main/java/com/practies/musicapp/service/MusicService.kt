package com.practies.musicapp.service

import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.TextView
import com.practies.musicapp.Music
import com.practies.musicapp.formatDuration
import java.util.concurrent.TimeUnit

class MusicService:Service(),MediaPlayer.OnPreparedListener ,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{
    var serviceMusicList=ArrayList<Music>()
    var songPosition:Int=0
    var isPlaying =false
      val interval=1000
    var playStat= STOPPED



    companion object{
        const val STOPPED=0
        const val PAUSED=1
        const val PLAYING=2
    }
    private lateinit var songs:Music
    lateinit var startPoint:TextView
    lateinit var entPoint:TextView

    private lateinit var  mediaPlayer:MediaPlayer
    private  val  myBinder=MyBinder()
    lateinit var seekBar: SeekBar
    override fun onBind(intent: Intent?): IBinder {

        return  myBinder

    }

    inner class     MyBinder:Binder(){
       val  service:MusicService
       get() = this@MusicService

    }

    override fun onCreate() {
        super.onCreate()

        mediaPlayer= MediaPlayer()
      //  createMediaPlayer()
          initMusic()
    }
    fun initMusic(){
        mediaPlayer.setWakeMode(applicationContext,PowerManager.PARTIAL_WAKE_LOCK)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
    }


    override fun onUnbind(intent: Intent?): Boolean {
       mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
        return false

    }
      //onPrepared
    override fun onPrepared(mp: MediaPlayer?) {
      mp!!.start()
        val duration=mp.duration
        seekBar.max=duration
        seekBar.postDelayed(progressRunner,interval.toLong())
        entPoint.text= formatDuration(mp.duration.toLong())
/*
String.format("%2d:%02d",
TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
TimeUnit.MINUTES.toSeconds(duration.toLong()))
*/
      }


    override fun onCompletion(mp: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }




   //  play music
    private fun playSong(){
        mediaPlayer.reset()
      val playSong=songs

      val currentSongId=playSong.id
      val trackUris=  ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currentSongId.toLong())

    //  mediaPlayer.setDataSource(applicationContext,trackUris)
       mediaPlayer.setDataSource(serviceMusicList[songPosition].path)
      mediaPlayer.prepareAsync()
      progressRunner.run()
    }


    private fun  createMediaPlayer(){

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(serviceMusicList[songPosition].path)
            mediaPlayer.prepare()
            mediaPlayer.start()
            isPlaying =true


        }catch (e :Exception){ return }

    }

 // setting the song  call it  first before  play song
    fun setSong(song: Music){
        songs=song
        playStat= PLAYING
        playSong()
    }
         //call this in playSceen
    fun setUI( seekBar: SeekBar,start_int:TextView,ent_int:TextView){
                this.seekBar=seekBar
          startPoint=start_int
          entPoint=ent_int


        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                }
                startPoint.text=String.format("%2d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(progress.toLong()),
                    TimeUnit.MINUTES.toSeconds(progress.toLong()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
    }
    private val progressRunner:Runnable=object :Runnable{
        override fun run() {
            seekBar.progress=mediaPlayer.currentPosition
            if (mediaPlayer.isPlaying){
                seekBar.postDelayed(this,interval.toLong())
            }

        }

    }




}

private fun MediaPlayer.setOnCompletionListener() {
    TODO("Not yet implemented")
}

