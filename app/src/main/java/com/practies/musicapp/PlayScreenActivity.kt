package com.practies.musicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
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


class PlayScreenActivity : AppCompatActivity() ,ServiceConnection{

//    lateinit var seekBar: SeekBar
//    lateinit var start_tv: TextView
//     lateinit var end_tv:TextView
//     lateinit var music: Music
var isPlaying:Boolean=false
         var songPostion :Int = 0
    var mediaPlayer :MediaPlayer ?=null
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


     // songPostion=  intent.getIntExtra("index",0)
    // musiclistPA=intent.getSerializableExtra("songList")  as ArrayList<Music>
       // Toast.makeText(this,"index  $songPostion",Toast.LENGTH_SHORT).show()




      //  startService(intent)
     // musicServices!!.playSong(musicServices!!.musiclistSe[index])
    }

    override fun onResume() {
        super.onResume()

        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updatUi( music: Music){
        setPlayLayout()

    }

    override fun onStart() {
//
        super.onStart()
          // createMediaPlayer()
        // PlayPauseButton
      //  bindingPlayScreen.playPauseButton.setOnClickListener{ if (isPlaying) pauseMusic() else    playMusic() }

        //NextSong
     //   bindingPlayScreen.nextButton.setOnClickListener{ nextPreviousSong(increment = true)}

        //Previous song

      //  bindingPlayScreen.priveButton.setOnClickListener{  nextPreviousSong(increment = false)}

       // setPlayLayout()

    }





    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
           val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()


      // musicServices!!.playSong(musicServices!!.musiclistSe[index])
        Toast.makeText(this,"play service connected   ",Toast.LENGTH_SHORT).show()


      //  musicServices!!.createMediaPlayer(musicServices!!.musiclistSe[index])
        //playSong(musicServices!!.musiclistSe[index])


    }

    override fun onServiceDisconnected(name: ComponentName?) {}
                 //for setting the current song image  and tittle
            fun setPlayLayout(){

//                Glide.with(this).load(musiclistPA[songPostion].artUri)
//                    .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
//                    .into(bindingPlayScreen.songImagePlay)
//                     bindingPlayScreen.songNamePlay.text= musiclistPA[songPostion].title
//                    bindingPlayScreen.songEnd.text=      formatDuration( musiclistPA[songPostion].duration)

            }

         fun  createMediaPlayer(){


              try { if (mediaPlayer==null) mediaPlayer= MediaPlayer()
                  mediaPlayer!!.reset()
                  mediaPlayer!!.setDataSource(musiclistPA[songPostion].path)
                  mediaPlayer!!.prepare()
                  mediaPlayer!!.start()
                  isPlaying =true
                  bindingPlayScreen.playPauseButton.setImageResource(R.drawable.pause_bt_circle)

              }catch (e :Exception){ return }

          }

    private fun playMusic(){
     //  isPlaying=true
        mediaPlayer!!.start()
        createMediaPlayer()
        bindingPlayScreen.playPauseButton.setImageResource(R.drawable.play_bt_circle)

    }
    private fun pauseMusic(){
        isPlaying = false
        mediaPlayer!!.pause()
        bindingPlayScreen.playPauseButton.setImageResource(R.drawable.pause_bt_circle)

    }
    //createMediaPlayer()
   // setPlayLayout()




/*

private val musicConnections :ServiceConnection = object :ServiceConnection {
override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

val binder: MusicService.MyBinder = service as MusicService.MyBinder
musicService = binder.service
musicService.setUI(
bindingPlayScreen.songSeekBar,
bindingPlayScreen.songStart,
bindingPlayScreen.songEnd
)
}


override fun onServiceDisconnected(name: ComponentName?) {
TODO("Not yet implemented")
}
}
*/


}