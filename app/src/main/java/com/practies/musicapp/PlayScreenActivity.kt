package com.practies.musicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.audiofx.AudioEffect
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.databinding.ActivityPlayScreen2Binding
import com.practies.musicapp.interfaces.OnSongComplete
import com.practies.musicapp.model.Music
import com.practies.musicapp.service.MusicServices
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

//MediaPlayer.OnPreparedListener
class PlayScreenActivity : AppCompatActivity() ,ServiceConnection ,OnSongComplete  {//,MediaPlayer.OnPreparedListener{

    lateinit var seekBar: SeekBar
      var active:Boolean=false
    companion object{
    var isFavorite:Boolean=false}

        var musicServices:MusicServices?=null
    //var  favoriteListPA= arrayListOf<Music>()

         lateinit var seekbarRunnable: Runnable

        lateinit var bindingPlayScreen: ActivityPlayScreen2Binding
    override fun onCreate(savedInstanceState: Bundle?) {

        bindingPlayScreen= ActivityPlayScreen2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bindingPlayScreen.root)
           active=true




        val intent =Intent(this, MusicServices::class.java)
       bindService(intent,this, BIND_AUTO_CREATE)
       startService(intent)




    }
    //call back from eventbus
  @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updateUi( music: Music){
        setPlayScreen(music)
      //  mainBinding.songNameMini.text=musicServices!!.musiclistSe[musicServices!!.currentIndex].title
        Log.i("MSG" ,"event bus called")

    }





    override fun onResume() {

        super.onResume()
         active=true
      EventBus.getDefault().register(this)

    }

    override fun onPause() {
        super.onPause()
         active=false
        EventBus.getDefault().unregister(this)

    }





    override fun onStart() {



       // updateUi(musicServices!!.musiclistSe[musicServices!!.currentIndex])

        seekBar= SeekBar(this)
        bindingPlayScreen.favButton.setOnClickListener{
           if (isFavorite){
               bindingPlayScreen.favButton.setImageResource(R.drawable._favorite_border)
               removeSongFromFavorite()
           }else{
               bindingPlayScreen.favButton.setImageResource(R.drawable.favorite_fill)
               currentSongAddToFavoriteList()
           }
        }

        bindingPlayScreen.nextButton.setOnClickListener { musicServices!!.nextPreviousSong(increment = true) }

        bindingPlayScreen.priveButton.setOnClickListener { musicServices!!.nextPreviousSong(increment = false) }

        bindingPlayScreen.playPauseButton.setOnClickListener {


            if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                bindingPlayScreen.playPauseButton.setImageResource(R.drawable.play_bt_circle)

            }else{
                musicServices!!.playPauseMusic(true)
                bindingPlayScreen.playPauseButton.setImageResource(R.drawable.pause_bt_circle)

            }
        }

        bindingPlayScreen.shuffleButton.setOnClickListener { musicServices!!.musiclistSe.shuffle()
            Toast.makeText(this,"Shuffle On",Toast.LENGTH_SHORT).show()
        }

        super.onStart()
    }



            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()
        musicServices!!.setListener(this)

                if (musicServices!!.mediaPlayer.isPlaying){ bindingPlayScreen.playPauseButton.setImageResource(R.drawable.pause_bt_circle)
                }else{ bindingPlayScreen.playPauseButton.setImageResource(R.drawable.play_bt_circle) }

                //*****************************************************************************************************************************************
               //if music list is not empty
                if (musicServices!!.musiclistSe .isNotEmpty()) updateUi(musicServices!!.musiclistSe[musicServices!!.currentIndex])      // try catch bloke needed

                 seekBarSetUp()
                 seekFunction()
                //*********************************************************************
      //  musicServices!!.showNotification(R.drawable.pause_bt_circle)

                bindingPlayScreen.repeatBt.setOnClickListener {      if(  musicServices!!.repeat){

                    musicServices!!.mediaPlayer.isLooping=false
                    bindingPlayScreen.repeatBt.setImageResource(R.drawable.repeat)

                    musicServices!!.repeat=false

                }else{
                    musicServices!!.mediaPlayer.isLooping=true
                    bindingPlayScreen.repeatBt.setImageResource(R.drawable.baseline_repeat_on)
                    musicServices!!.repeat=true
                }
                }
                bindingPlayScreen.equalizerBt.setOnClickListener {

                       try {
                           val eqIntent=Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                           eqIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,musicServices!!.mediaPlayer.audioSessionId)
                           eqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME,baseContext.packageName)
                           eqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE,AudioEffect.CONTENT_TYPE_MUSIC)
                           startActivityForResult(eqIntent,11)
                       }catch (e:Exception){  Toast.makeText(this,"Equalizer feature not supported",Toast.LENGTH_SHORT).show()}

                }

        Log.i("MSg","Serveise  connected with playScreen")



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
//
                     if (checkTheSongIsInFavourites(music.id)){
                      bindingPlayScreen.favButton.setImageResource(R.drawable.favorite_fill)
                  }else{ bindingPlayScreen.favButton.setImageResource(R.drawable._favorite_border) }

                     //try catch need for update playScreen for playList*****************************
                     //***********************************************************************88
                    if(active) {
                        Glide.with(this).load(music.artUri)
                            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
                            .into(bindingPlayScreen.songImagePlay)
                        bindingPlayScreen.songNamePlay.text = music.title
                        bindingPlayScreen.songEnd.text = formatDuration(music.duration)
                    }

            }
           //move this function to service
    private fun checkTheSongIsInFavourites(id:String): Boolean {
                    isFavorite=false
                musicServices!!.favoritelistSe.forEachIndexed { index, favoriteMusic ->

                    if (id==favoriteMusic.id){
                        isFavorite =true
                        return true
                    }
                }

        return false
    }

                        override fun onSongComplete() {
                              if (musicServices!!.currentIndex == musicServices!!.musiclistSe.size-1)

                              { musicServices!!.currentIndex= 0 }else{

                                  musicServices!!.currentIndex ++
                                  updateUi(musicServices!!.musiclistSe[musicServices!!.currentIndex])
                                  musicServices!!.playSong()
                                  } }


   @DelicateCoroutinesApi
   private fun currentSongAddToFavoriteList(){


       val curretSong=musicServices!!.musiclistSe[musicServices!!.currentIndex]
       curretSong.play_list_name= favorite
       curretSong.timeStamp=System.currentTimeMillis().toString()+curretSong.id
        val  favoriteMusic= Music(
            curretSong.timeStamp,
            curretSong.id,curretSong.title,curretSong.album,curretSong.artist,
            curretSong.duration,curretSong.path,curretSong.artUri,curretSong.play_list_name,
        )
         //add to data base
       GlobalScope.launch (Dispatchers.IO){   musicServices!!.favMusicDa.addSong(favoriteMusic)         //.addSong(favoriteMusic)
           Log.i("Favourites", "Song added")


          // Log.i("IMPo",mPlaylist.toString())
       }
       bindingPlayScreen.favButton.setImageResource(R.drawable.favorite_fill)

   }

         private fun removeSongFromFavorite(){

             val curretSong=musicServices!!.musiclistSe[musicServices!!.currentIndex]

             val  favoriteMusic= Music( curretSong.timeStamp,
                 curretSong.id,curretSong.title,curretSong.album,curretSong.artist,
                 curretSong.duration,curretSong.path,curretSong.artUri,curretSong.play_list_name)
             GlobalScope.launch (Dispatchers.IO){ musicServices!!.favMusicDa.deleteSong(favoriteMusic) }

             bindingPlayScreen.favButton.setImageResource(R.drawable._favorite_border)
         }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==11 ||resultCode== RESULT_OK)

            return

    }

    override fun onDestroy() {
        super.onDestroy()
        active=false
    }










}










