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
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.practies.musicapp.Music
import com.practies.musicapp.formatDuration
import org.greenrobot.eventbus.EventBus
import java.io.File


//,MediaPlayer.OnPreparedListener , mediaPlayer= MediaPlayer(),MediaPlayer.OnErrorListener
class MusicServices :Service(),MediaPlayer.OnCompletionListener{
     var songPosition=3
    var currentIndex =0
    var songPosionSe=0
    var isPlaying=false
   // var serviceMusicList= mutableListOf<Music>()
    private lateinit var mediaPlayer: MediaPlayer



    lateinit var mediaSession:MediaSessionCompat
    private var mybinder=Mybinder()
    var  musiclistSe=  arrayListOf<Music>()              //mutableListOf<Music>() //arrayListOf<Music>()

       override fun onBind(intent: Intent?): IBinder {
           mediaSession = MediaSessionCompat(baseContext, "My Music")
           return mybinder}

    inner class  Mybinder:Binder(){
        fun currentService(): MusicServices {
            return  this@MusicServices }}

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()

    }

    fun playSong(){

        try {
            mediaPlayer=MediaPlayer()
            mediaPlayer.reset()
            mediaPlayer.setDataSource(musiclistSe[currentIndex].path)
            Log.i("Music"," music list is ok")
            mediaPlayer.prepare()
            mediaPlayer.start()
           // EventBus.getDefault().post(musiclistSe[currentIndex])
            isPlaying =true
            //  binding.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)

        }catch (e :Exception){ return }


    }



    fun initMediaPlayer(){
        mediaPlayer= MediaPlayer()
        mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnCompletionListener(this)

    }

    override fun onCompletion(mp: MediaPlayer?) {
        //++currentIndex


        TODO("Not yet implemented")//nextSong
    }

    private  fun  nextPreviousSong(increment: Boolean){
        if (increment){
            setSongPosition(increment= true)


        }else{
            setSongPosition(increment=false)

        }

    }

    private fun setSongPosition(increment: Boolean){
        if(increment){

            if(musiclistSe.size-1==currentIndex){ currentIndex=0 }
            else
                ++currentIndex }

        else{

            if (currentIndex==0){ currentIndex= musiclistSe.size-1 }
            else
                --currentIndex }

    }


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





























}


















