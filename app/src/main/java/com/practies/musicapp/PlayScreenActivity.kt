package com.practies.musicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.databinding.ActivityPlayScreen2Binding
import com.practies.musicapp.service.MusicServices
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

                    //MediaPlayer.OnPreparedListener
class PlayScreenActivity : AppCompatActivity() ,ServiceConnection,MediaPlayer.OnPreparedListener{

 // lateinit var seekBar: SeekBar
  val intervell=1000
//    lateinit var start_tv: TextView
//     lateinit var end_tv:TextView
//     lateinit var music: Music
var PLAY:Boolean=false
         var songPostion :Int = 0
   // var mediaPlayer :MediaPlayer ?=null
     var musicServices:MusicServices?=null
    var  musiclistPA= arrayListOf<Music>()

        lateinit var bindingPlayScreen: ActivityPlayScreen2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        bindingPlayScreen= ActivityPlayScreen2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bindingPlayScreen.root)


        val intent =Intent(this, MusicServices::class.java)
       bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
       startService(intent)
        Intent(this,MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)




    }

    override fun onResume() {
        super.onResume()
        PLAY=true
        EventBus.getDefault().register(this)
        Toast.makeText(this,"Event bus regesterd",Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updateUi( music: Music){
        setPlayLayout(music)

          Log.i("MSG","event bus called")

    }

    override fun onStart() {

        bindingPlayScreen.nextButton.setOnClickListener { musicServices!!.nextPreviousSong(increment = true) }

        bindingPlayScreen.priveButton.setOnClickListener {
            musicServices!!.nextPreviousSong(
                increment = false
            )
        }

        bindingPlayScreen.playPauseButton.setOnClickListener {


            if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                bindingPlayScreen.playPauseButton.setImageResource(R.drawable.pause_bt_circle)

            }else{
                musicServices!!.playPauseMusic(true)
                bindingPlayScreen.playPauseButton.setImageResource(R.drawable.play_bt_circle)

            }



        }

        //  To  seek the seekBar
               bindingPlayScreen.songSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                   override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                      // if (fromUser)musicServices!!.mediaPlayer.seekTo(progress)
                       if (fromUser) musicServices!!.mediaPlayer.seekTo(progress*1000)

                   }

                   override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                   override fun onStopTrackingTouch(seekBar: SeekBar?) =Unit

               })


        super.onStart()
    }





    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()



    }

    override fun onServiceDisconnected(name: ComponentName?) {}
                 //for setting the current song image  and tittle
            fun setPlayLayout(music: Music){
                Glide.with(this).load(music.artUri)
                    .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
                    .into(bindingPlayScreen.songImagePlay)
                     bindingPlayScreen.songNamePlay.text= music.title
                    bindingPlayScreen.songEnd.text=      formatDuration(music.duration)

            }

    override fun onPrepared(mp: MediaPlayer?) {

                 mp!!.start()
        val duration=mp.duration
          musicServices!!.seekBar.max=duration
     //   musicServices!!.seekBar.max=(duration/1000)
        musicServices!!.seekBar.postDelayed(progressRunner,intervell.toLong())
    }
private val progressRunner= object :Runnable{
    override fun run() {
        musicServices!!.seekBar.progress=musicServices!!.mediaPlayer.currentPosition
        if (musicServices!!.mediaPlayer.isPlaying){
            musicServices!!.seekBar.postDelayed(this,intervell.toLong())
        }
    }}



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
//           seekBar.progress=   musicServices.mediaPlayer.currentPosition
//                if (mediaPlayer.isPlaying){
//                    seekBar.postDelayed(this,interval.toLong())
//                }
//        } }




}