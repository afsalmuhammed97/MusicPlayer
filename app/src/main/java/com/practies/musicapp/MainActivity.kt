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
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.practies.musicapp.adapter.ViewPageAdapter
import com.practies.musicapp.databinding.ActivityMainBinding

import java.io.File

class MainActivity : AppCompatActivity(),ServiceConnection {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val intent = Intent(this, MusicService::class.java)
//        bindService(intent,this, BIND_AUTO_CREATE)
//        startService(intent)


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
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }
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









