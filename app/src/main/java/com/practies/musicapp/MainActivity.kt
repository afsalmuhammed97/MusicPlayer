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
import android.view.Menu
import android.widget.SearchView
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity(),ServiceConnection {

     lateinit var mainBinding: ActivityMainBinding
    var musicServices:MusicServices?=null
      //private lateinit var searchAdapter: SearchAdapter
     lateinit var  recentSong:Music
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val intent = Intent(this, MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)


        val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE)
        val jSonString = editor.getString("LastPlayedSong",null)
        val typeToken =object : TypeToken<Music>(){}.type

        if (jSonString != null){
          recentSong = GsonBuilder().create().fromJson(jSonString,typeToken)
            Log.i("RecentSong:::",recentSong.title)
        }



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
        setMiniPlayerScreen(music)
       //nBinding.songNameMini.text=musicServices //  mai!!.musiclistSe[musicServices!!.currentIndex].title
        Log.i("MSG" ,"event bus called")

    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    private fun setMiniPlayerScreen(music: Music) {
        mainBinding.songNameMini.text=music.title

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()

     //   musicServices.setSongList()

        if (musicServices!!.musiclistSe.isNotEmpty()) updateUi(musicServices!!.musiclistSe[musicServices!!.currentIndex])

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
        val searchView:SearchView
       val alertDialog=AlertDialog.Builder(this)
        val customAlert=LayoutInflater.from(this).inflate(R.layout.search_list_view,mainBinding.root,false)
        val searchText=customAlert.findViewById<TextInputEditText>(R.id.searchText)
//        val searchRv= customAlert.findViewById<RecyclerView>
        //val fragment:AlSongsFragment=

            //fragment no null
                  //  supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager2.toString() + ":0")   as AlSongsFragment



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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu,menu )
           val searchView=menu?.findItem(R.id.search_view )?.actionView as SearchView
           searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

               override fun onQueryTextSubmit(query: String?): Boolean =true

               override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@MainActivity,newText.toString(),Toast.LENGTH_SHORT).show()
                return  true

               }

           })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("IMp","OnDistroy called")
        val lastplayedSong=musicServices!!.musiclistSe[musicServices!!.currentIndex]

        Log.i("LastSong in serviese",lastplayedSong.toString())
      //  Toast.makeText(baseContext, lastplayedSong.title,Toast.LENGTH_LONG).show()
        val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
        val jSonString= GsonBuilder().create().toJson(lastplayedSong)
        editor.putString("LastPlayedSong",jSonString,)
        editor.apply()


    }





}















