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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.database.FavoriteDataBase
import com.practies.musicapp.database.FavoriteDataBase.Companion.getDatabase
import com.practies.musicapp.database.MusicDao
import com.practies.musicapp.databinding.ActivityPlayScreen2Binding
import com.practies.musicapp.interfaces.OnSongComplete
import com.practies.musicapp.model.FavoriteMusic
import com.practies.musicapp.service.MusicServices
import com.practies.musicapp.service.MusicServices.Companion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

//MediaPlayer.OnPreparedListener
class PlayScreenActivity : AppCompatActivity() ,ServiceConnection ,OnSongComplete  {//,MediaPlayer.OnPreparedListener{
  //   lateinit var favMusicDao:MusicDao
    lateinit var seekBar: SeekBar
    val intervell=1000
    lateinit var startPoint:TextView
    lateinit var entPoint:TextView
    var isFavorite:Boolean=false
    companion object{
   // var isFavorite:Boolean=false
    var favIndex:Int=0
}
  // lateinit var musicViewModel: MusicViewModel

     var musicServices:MusicServices?=null
    var  favoriteListPA= arrayListOf<Music>()

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
//  View Model of Database******************************************

//       musicViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MusicViewModel::class.java)
//        favMusicDao =FavoriteDataBase.getDatabase(this).musicDao()
        //getDatabase(requireActivity().application).musicDao()





    }
    //call back from eventbus
    @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updateUi( music: Music){
        setPlayScreen(music)

//      FavoriteMusic(music.id,music.title          /***   add to favorite databa
//            ,music.album,music.artist,music.duration,
//            music.path,music.artUri,music.playListId)
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

        bindingPlayScreen.favButton.setOnClickListener{

                            favaIconChang()
            currentSongAddToFavoriteList()
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

        super.onStart()
    }
            fun favaIconChang(){
                if ( !isFavorite) {
                    bindingPlayScreen.favButton.setImageResource(R.drawable.favorite_fill)
                    // musicServices!!.favoritelistSe.add(musicServices!!.musiclistSe[musicServices!!.currentIndex])
                   // Toast.makeText(this,"true",Toast.LENGTH_SHORT).show()

                 //  currentSongAddToFavoriteList()
                    isFavorite=true


                }else {
                    bindingPlayScreen.favButton.setImageResource(R.drawable._favorite_border)
                    // musicServices!!.favoritelistSe.removeAt(favIndex)
                    Toast.makeText(this, "false", Toast.LENGTH_SHORT).show()

                    isFavorite = false
                }

            }



    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()
        musicServices!!.setListener(this)
        seekBarSetUp()
        seekFunction()
         musicServices!!.showNotification()


        //to check the current song is favorite or not
//             val tempIndex   = musicServices!!.favoriteChecker(musicServices!!.musiclistSe[musicServices!!.currentIndex].id)
//              if (tempIndex != -1) favIndex=tempIndex
//
//        if (isFavorite) {
//          //  bindingPlayScreen.favButton.setImageResource(R.drawable.favorite_fill)
//        }else
//          //  bindingPlayScreen.favButton.setImageResource(R.drawable._favorite_border)
//            Toast.makeText(this,"fav index ${favIndex} ",Toast.LENGTH_SHORT).show()

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


   private fun currentSongAddToFavoriteList(){
     //  val     musicViewModel= ViewModelProvider(this )[MusicViewModel::class.java]

       val curretSong=musicServices!!.musiclistSe[musicServices!!.currentIndex]
        val  favoriteMusic=FavoriteMusic(
            curretSong.id,curretSong.title,curretSong.album,curretSong.artist,
            curretSong.duration,curretSong.path,curretSong.artUri,curretSong.playListId
        )
         //add to data base
       GlobalScope.launch (Dispatchers.IO){   musicServices!!.favMusicDao.addSong(favoriteMusic)
           Log.i("Favourites", "Song added")

//              musicServices!!.favoritelistSe=   musicServices!!.favMusicDao.readAllSongs()


       }

      //  musicViewModel.addSong(favoriteMusic)
     //   Toast.makeText(this,"the song is added",Toast.LENGTH_SHORT).show()



    }




                        }





