package com.practies.musicapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.practies.musicapp.adapter.ViewPageAdapter
import com.practies.musicapp.databinding.ActivityMainBinding
import com.practies.musicapp.fragments.AlSongsFragment
import com.practies.musicapp.model.Music
import com.practies.musicapp.service.MusicServices
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import androidx.viewpager.widget.ViewPager







class MainActivity : AppCompatActivity(),ServiceConnection {

     lateinit var mainBinding: ActivityMainBinding
    var musicServices:MusicServices?=null
      //private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val intent = Intent(this, MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)


        val adapter=ViewPageAdapter(supportFragmentManager, lifecycle)
        mainBinding.viewPager2.adapter = adapter

        TabLayoutMediator( mainBinding.tabLayout,mainBinding.viewPager2 ) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "songs"
                }
                1 -> {
                    tab.text = "favourite"
                }
                2 -> {
                    tab.text = "Playlist"
                }
            }
        }.attach()



    }

    @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updateUi( music: Music){
        setPlayScreen(music)
       //nBinding.songNameMini.text=musicServices //  mai!!.musiclistSe[musicServices!!.currentIndex].title
        Log.i("MSG" ,"event bus called")

    }

    private fun setPlayScreen(music: Music) {
        mainBinding.songNameMini.text=music.title

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()

        mainBinding.searchBt.setOnClickListener {
              showSearch()
          }


        mainBinding.nextMiniBt.setOnClickListener{
            musicServices!!.nextPreviousSong(true)
        }
        mainBinding.playPauseMiniBt.setOnClickListener{

            if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                mainBinding.playPauseMiniBt.setImageResource(R.drawable.play_bt_circle)

            }else{
                musicServices!!.playPauseMusic(true)
                mainBinding.playPauseMiniBt.setImageResource(R.drawable.pause_bt_circle)

            }
        }
        mainBinding.prevMiniBt.setOnClickListener{
            musicServices!!.nextPreviousSong(false)
        }
      //  binding.songNameMini.text=songCurrentTitle
        mainBinding.miniPlayerLayout.setOnClickListener{
            //move  to the  player screen

            try {
                if (musicServices !=null) {
                    val intent = Intent(this, PlayScreenActivity::class.java)
                    startActivity(intent)
                    Log.i("Main", "reopen play screen")
                }

            }catch (e:Exception){ Toast.makeText(this,"media player would not start yet",Toast.LENGTH_SHORT).show()  }
        }

//       // binding.songNameMini.text=musicServices!!.musiclistSe[musicServices!!.currentIndex].title


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearch() {
       val alertDialog=AlertDialog.Builder(this)
        val customAlert=LayoutInflater.from(this).inflate(R.layout.search_list_view,mainBinding.root,false)
//        val searchText=customAlert.findViewById<TextInputEditText>(R.id.searchText)
//        val searchRv= customAlert.findViewById<RecyclerView>
        val fragment:AlSongsFragment=
            supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager2.toString() + ":0")   as AlSongsFragment

                   //fragment no null


//        searchAdapter= SearchAdapter()
//        searchAdapter.notifyDataSetChanged()
//        searchRv.layoutManager=LinearLayoutManager(this)
//        searchRv.adapter=searchAdapter
    alertDialog.setView(customAlert)
     alertDialog.setNegativeButton("Cancel"){ dialogInterface:DialogInterface,i:Int ->
         dialogInterface.cancel()
     }
        alertDialog.create()
        alertDialog.show()









    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }






}















