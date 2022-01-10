package com.practies.musicapp.activities

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
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.GsonBuilder
import com.practies.musicapp.R
import com.practies.musicapp.adapter.ViewPageAdapter
import com.practies.musicapp.databinding.ActivityMainBinding
import com.practies.musicapp.model.model2.LastPlayed
import com.practies.musicapp.model.model2.Music
import com.practies.musicapp.service.MusicServices
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(),ServiceConnection {
    val list= ArrayList<String>()
    //val searchList= arrayListOf<Music>()
     lateinit var mainBinding: ActivityMainBinding
    var musicServices:MusicServices?=null
    //  private lateinit var searchAdapter: SearchAdapter
   //  lateinit var  recentSong:Music
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
                0 -> { tab.text="Songs"


                }
                1 -> {
                    tab.text =  "favourite"
                }
                2 -> {
                    tab.text ="Playlist"
                }


            }
        }.attach()

       Log.i("testList",list.toString())

    }

    @Subscribe(threadMode=  ThreadMode.MAIN)
    fun updateUi( music: Music){
        setMiniPlayerScreen(music)
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
        //  musicServices!!.  initMediaPlayer()
     //   musicServices.setSongList()

        if (musicServices!!.musiclistSe.isNotEmpty()) updateUi(musicServices!!.musiclistSe[musicServices!!.currentIndex])




        mainBinding.nextMiniBt.setOnClickListener{
            musicServices!!.nextPreviousSong(true)
            mainBinding.playPauseMiniBt.setImageResource(R.drawable.pause_circle_mini)

        }
        mainBinding.playPauseMiniBt.setOnClickListener{

            if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                mainBinding.playPauseMiniBt.setImageResource(R.drawable.play_circle_mini)

            }else{
                musicServices!!.playPauseMusic(true)
                mainBinding.playPauseMiniBt.setImageResource(R.drawable.pause_circle_mini)

            }
        }
        mainBinding.prevMiniBt.setOnClickListener{
            musicServices!!.nextPreviousSong(false)
            mainBinding.playPauseMiniBt.setImageResource(R.drawable.pause_circle_mini)

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
      //  val searchView:SearchView
       val alertDialog=AlertDialog.Builder(this)
        val customAlert=LayoutInflater.from(this).inflate(R.layout.search_list_view,mainBinding.root,false)
        //val searchText=customAlert.findViewById<TextInputEditText>(R.id.searchText)
       // val searchRv= customAlert.findViewById<RecyclerView>(R.id.searchListRv)
//
//
//
//            //fragment no null
//
//                          fragment =
//                              supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager2.toString() + ":0") as AlSongsFragment
//        val fragment:AlSongsFragment=
//
//           searchText.addTextChangedListener(object :TextWatcher{
//               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                 // Toast.makeText(this@MainActivity,s,Toast.LENGTH_SHORT).show()
//                 if(s != null){
//                     val userInput= s.toString().lowercase(Locale.getDefault())
//                     for (song  in fragment.musiclist){
//                         if (song.title.lowercase().contains(userInput)){
//                             searchList.add(song)
//
//                         }
//                     }
//                 }
//
//               }
//
//               override fun afterTextChanged(s: Editable?) {}
//
//           })


//        searchAdapter= SearchAdapter(searchList)
//        searchAdapter.notifyDataSetChanged()
//        searchRv.layoutManager= LinearLayoutManager(this)
//        searchRv.hasFixedSize()
//        searchRv.setItemViewCacheSize(8)
//        searchRv.adapter=searchAdapter
//
//        searchAdapter.setOnItemClickLisnter(object : MusicAdapter.onItemClickListener{
//            override fun onItemClick(position: Int) {
//                Toast.makeText(this@MainActivity,"${position} the song clicked",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onOptionClick(position: Int, itemview: View) {
//                TODO("Not yet implemented")
//            }
//
//        })



    alertDialog.setView(customAlert)
     alertDialog.setNegativeButton("Cancel"){ dialogInterface:DialogInterface,i:Int ->
         dialogInterface.cancel()
     }
        alertDialog.create()
        alertDialog.show()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onDestroy() {

        Log.i("IMp","OnDistroy called")
        val song =musicServices!!.musiclistSe[musicServices!!.currentIndex]

        val lastplayedSong:LastPlayed
        lastplayedSong=
            LastPlayed(timeStamp = song.timeStamp, id = song.id, title = song.title, artist = song.artist,
            album = song.album, duration = song.duration, path = song.path, artUri = song.artUri,
            play_list_name = song.play_list_name, songIndex = musicServices!!.currentIndex)

        Log.i("LastSong in serviese",lastplayedSong.toString())
      //  Toast.makeText(baseContext, lastplayedSong.title,Toast.LENGTH_LONG).show()
        val editor=getSharedPreferences("RESENT_SONG", MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
        val jSonString= GsonBuilder().create().toJson(lastplayedSong)
        editor.putString("LastPlayedSong",jSonString,)
        editor.apply()


        super.onDestroy()

    }

//    override fun sendData(message: String?) {
//      val tag="android:switcher:" +R.id.view_pager2.toString() +":"+0
//        val frag=supportFragmentManager.findFragmentByTag(tag) as SearchFragment?
//        if (frag != null) {
//            frag.displayReceivedData(message!!)
//        }

      //  val currentItem=getItem(1 )

        //view_pager2.currentItem=currentItem

  //  fun getItem(i:Int)=view_pager2.currentItem+i

}















