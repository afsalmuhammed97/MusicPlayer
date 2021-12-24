package com.practies.musicapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.practies.musicapp.adapter.SearchAdapter

import com.practies.musicapp.adapter.ViewPageAdapter
import com.practies.musicapp.databinding.ActivityMainBinding
import com.practies.musicapp.model.Music
import com.practies.musicapp.service.MusicServices
import com.practies.musicapp.service.MusicServices.Companion.songCurrentTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),ServiceConnection {

    private lateinit var binding: ActivityMainBinding
     var musicServices:MusicServices?=null
      private lateinit var searchAdapter: SearchAdapter
    val songList=ArrayList<Music>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)


        val adapter=ViewPageAdapter(supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter

        TabLayoutMediator( binding.tabLayout,binding.viewPager2 ) { tab, position ->

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

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()
           musicServices!!.tempListSe=songList
          binding.searchBt.setOnClickListener {
              showSearch()
          }


        binding.nextMiniBt.setOnClickListener{
            musicServices!!.nextPreviousSong(true)
        }
        binding.playPauseMiniBt.setOnClickListener{

            if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                binding.playPauseMiniBt.setImageResource(R.drawable.play_bt_circle)

            }else{
                musicServices!!.playPauseMusic(true)
                binding.playPauseMiniBt.setImageResource(R.drawable.pause_bt_circle)

            }
        }
        binding.prevMiniBt.setOnClickListener{
            musicServices!!.nextPreviousSong(false)
        }
      //  binding.songNameMini.text=songCurrentTitle
        binding.miniPlayerLayout.setOnClickListener{
            //move  to the  player screen
            if (musicServices !=null) {
                val intent = Intent(this, PlayScreenActivity::class.java)
                startActivity(intent)
                Log.i("Main", "reopen play screen")
            }
        }

       // binding.songNameMini.text=musicServices!!.musiclistSe[musicServices!!.currentIndex].title


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearch() {
       val alertDialog=AlertDialog.Builder(this)
        val customAlert=LayoutInflater.from(this).inflate(R.layout.search_list_view,binding.root,false)
        val searchText=customAlert.findViewById<TextInputEditText>(R.id.searchText)
        val searchRv= customAlert.findViewById<RecyclerView>(R.id.searchListRv)



        searchAdapter= SearchAdapter(songList)
        searchAdapter.notifyDataSetChanged()
        searchRv.layoutManager=LinearLayoutManager(this)
        searchRv.adapter=searchAdapter
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

    ///mini player functions






}








//   override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode== 11){
//            if (grantResults.isNotEmpty()&& grantResults[0] ==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show()
//
//                //   initializeLayouts()
//
//
//            }else
//
//                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),11)
//        }
//    }




//   private fun  requestRuntimePermissions():Boolean{
//
//
//        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),11)
//            return false
//        }
//        return true
//    }









