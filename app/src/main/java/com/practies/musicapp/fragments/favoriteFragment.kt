package com.practies.musicapp.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.Music
import com.practies.musicapp.R
import com.practies.musicapp.adapter.FavoriteAdapter
import com.practies.musicapp.database.FavoriteDataBase
import com.practies.musicapp.database.MusicDao
import com.practies.musicapp.databinding.FragmentFavoriteBinding
import com.practies.musicapp.model.FavoriteMusic
import com.practies.musicapp.service.MusicServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class favoriteFragment : Fragment(),ServiceConnection {
    lateinit var favMusicDao: MusicDao
     var musicServices:MusicServices?=null
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favAdapter:FavoriteAdapter
    var  favoriteList= mutableListOf<FavoriteMusic>()
         // var favoriteSongs:MutableList<FavoriteMusic> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
              val intent=Intent(context,MusicServices::class.java)
        requireActivity().bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
        requireActivity().startService(intent)
             favMusicDao= FavoriteDataBase.getDatabase(requireActivity().application).musicDao()
             GlobalScope.launch (Dispatchers.IO){

                 favoriteList=favMusicDao.readAllSongs()
                 Log.i("Fav Frag",favoriteList.toString())
             }


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        favMusicDao= FavoriteDataBase.getDatabase(requireActivity().application).musicDao()
       // /getDatabase(requireActivity().application).musicDao()









            binding= FragmentFavoriteBinding.inflate(inflater,container,false)

        val tempList=ArrayList<String>()
        tempList.add("song name 1 ")
        tempList.add("song name 2")

        tempList.add("song name 3")

        tempList.add("song name 4")

        tempList.add("song name 5")

        tempList.add("song name 6")

        tempList.add("song name 7")
        tempList.add("song name 8")
        // Log.i("In Favorite",favoriteList.toString())

       favAdapter= FavoriteAdapter (favoriteList)//(favoriteList )
        binding.favRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.favRecyclerView.hasFixedSize()
        binding.favRecyclerView.setItemViewCacheSize(13)
       binding.favRecyclerView.adapter=favAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        favAdapter.setOnItemClickListner(object :FavoriteAdapter.onItemClickListner{
//            override fun onItemClick(position: Int) {
//              Toast.makeText(context,"item ${position} clicked",Toast.LENGTH_SHORT).show()
//            }

   //     })

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()

        Toast.makeText(context,"Favorite connected",Toast.LENGTH_SHORT).show()
       // musicServices!!.favoritelistSe=favoriteList
//        GlobalScope.launch (Dispatchers.IO){
//            musicServices!!.favoritelistSe=   musicServices!!.favMusicDao.readAllSongs()
//           // Log.i("Fav Frag",musicServices!!.favoritelistSe.toString())
//        }



    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }


}