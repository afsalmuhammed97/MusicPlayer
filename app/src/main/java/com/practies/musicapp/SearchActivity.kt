package com.practies.musicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.practies.musicapp.databinding.ActivitySearchBinding
import com.practies.musicapp.service.MusicServices

class SearchActivity : AppCompatActivity(),ServiceConnection {
 lateinit var  binding:ActivitySearchBinding
    var musicServices: MusicServices?=null
    override fun onCreate(savedInstanceState: Bundle?) {
   binding= ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = Intent(this, MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)



    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
      val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }
}