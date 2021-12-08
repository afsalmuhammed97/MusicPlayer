package com.practies.musicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.databinding.ActivityPlayScreen2Binding
import com.practies.musicapp.interfaces.OnSongComplete
import com.practies.musicapp.service.MusicServices
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

//MediaPlayer.OnPreparedListener
class PlayScreenActivity : AppCompatActivity() ,ServiceConnection ,OnSongComplete  {//,MediaPlayer.OnPreparedListener{

                       lateinit var seekBar: SeekBar
                      val intervell=1000
                        lateinit var startPoint:TextView
                        lateinit var entPoint:TextView

   // var mediaPlayer :MediaPlayer ?=null
     var musicServices:MusicServices?=null
    var  musiclistPA= arrayListOf<Music>()
         lateinit var seekbarRunnable: Runnable
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
    @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updateUi( music: Music){
        setPlayScreen(music)
//          seekBarSetUp()
//        seekFunction()
        Log.i("MSG","event bus called")

    }

    override fun onResume() {
        super.onResume()

      EventBus.getDefault().register(this)
        Toast.makeText(this,"Event bus regesterd",Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)

    }



    override fun onStart() {
         seekBar= SeekBar(this)
        bindingPlayScreen.nextButton.setOnClickListener { musicServices!!.nextPreviousSong(increment = true) }

        bindingPlayScreen.priveButton.setOnClickListener { musicServices!!.nextPreviousSong(increment = false) }

        bindingPlayScreen.playPauseButton.setOnClickListener {


            if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                bindingPlayScreen.playPauseButton.setImageResource(R.drawable.play_bt_circle)

            }else{
                musicServices!!.playPauseMusic(true)
                bindingPlayScreen.playPauseButton.setImageResource(R.drawable.pause_bt_circle)

            } }



        super.onStart()
    }





    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()
        musicServices!!.setListener(this)
        seekBarSetUp()
        seekFunction()

    }

    override fun onServiceDisconnected(name: ComponentName?) {}
                private fun seekFunction(){

                    bindingPlayScreen.songSeekBar.setOnSeekBarChangeListener( object :SeekBar.OnSeekBarChangeListener{
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean
                        ) {
                           if (fromUser) musicServices!!.mediaPlayer.seekTo(progress)
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) =Unit

                        override fun onStopTrackingTouch(seekBar: SeekBar?)=Unit

                    })

                }

                    private fun seekBarSetUp(){
                        seekbarRunnable= Runnable {
                            bindingPlayScreen.songStart.text= formatDuration(musicServices!!.mediaPlayer.currentPosition.toLong())
                            bindingPlayScreen.songSeekBar.progress=musicServices!!.mediaPlayer.currentPosition
                            bindingPlayScreen.songSeekBar.max=musicServices!!.mediaPlayer.duration
                            Handler(Looper.getMainLooper()).postDelayed(seekbarRunnable,1000)
                        }
                        Handler(Looper.getMainLooper()).postDelayed(seekbarRunnable,1000)
                    }

                 //for setting the current song image  and tittle
            fun setPlayScreen(music: Music){
                Glide.with(this).load(music.artUri)
                    .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
                    .into(bindingPlayScreen.songImagePlay)
                     bindingPlayScreen.songNamePlay.text= music.title
                    bindingPlayScreen.songEnd.text=      formatDuration(music.duration)
                   //  bindingPlayScreen.songStart.text=

            }


                        override fun onSongComplete() {
                              if (musicServices!!.currentIndex == musicServices!!.musiclistSe.size-1)

                              { musicServices!!.currentIndex= 0 }else{

                                  musicServices!!.currentIndex ++
                                        updateUi(musicServices!!.musiclistSe[musicServices!!.currentIndex])
                                      musicServices!!.playSong()
                                  } }



                        }

//    override fun onPrepared(mp: MediaPlayer?) {
//
//                 mp!!.start()
//        val duration=mp.duration
//          musicServices!!.seekBar.max=duration
//     //   musicServices!!.seekBar.max=(duration/1000)
//        musicServices!!.seekBar.postDelayed(progressRunner,intervell.toLong())
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
//           seekBar.progress=   musicServices.mediaPlayer.currentPosition
//                if (mediaPlayer.isPlaying){
//                    seekBar.postDelayed(this,interval.toLong())
//                }
//        } }




