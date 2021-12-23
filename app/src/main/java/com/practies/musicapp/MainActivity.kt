package com.practies.musicapp

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.practies.musicapp.adapter.ViewPageAdapter
import com.practies.musicapp.databinding.ActivityMainBinding
import com.practies.musicapp.service.MusicServices
import com.practies.musicapp.service.MusicServices.Companion.songCurrentTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),ServiceConnection {

    private lateinit var binding: ActivityMainBinding
     var musicServices:MusicServices?=null


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









