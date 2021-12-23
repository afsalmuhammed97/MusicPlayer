package com.practies.musicapp.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.model.Music
import com.practies.musicapp.R
import com.practies.musicapp.adapter.FavoriteAdapter
import com.practies.musicapp.databinding.FragmentFavoriteBinding
import com.practies.musicapp.favorite
import com.practies.musicapp.musicDatabase.MusicDao
import com.practies.musicapp.musicDatabase.MusicDatabase
import com.practies.musicapp.service.MusicServices
import kotlinx.coroutines.*
import kotlinx.coroutines.DelicateCoroutinesApi as DelicateCoroutinesApi1


class favoriteFragment : Fragment(),ServiceConnection {
    lateinit var favMusicDao: MusicDao
     var musicServices:MusicServices?=null
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favAdapter:FavoriteAdapter
    var  favoriteList= arrayListOf<Music>()
    var isShuffle:Boolean=false
var temp=ArrayList<Music>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
              val intent=Intent(context,MusicServices::class.java)
        requireActivity().bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
        requireActivity().startService(intent)
             favMusicDao=MusicDatabase.getDatabase(requireActivity().application).musicDao()


        GlobalScope.launch(Dispatchers.IO) {       favoriteList=favMusicDao.readAllFavoriteSong()  as ArrayList<Music>
            Log.i("Fav Frag", favoriteList.toString())


        }


///////////////////////////////////////////////////readAllFavoriteSong()
    }





    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        favMusicDao= FavoriteDataBase.getDatabase(requireActivity().application).musicDao()
       // /getDatabase(requireActivity().application).musicDao()









            binding= FragmentFavoriteBinding.inflate(inflater,container,false)

        // Log.i("In Favorite",favoriteList.toString())

       favAdapter= FavoriteAdapter (favoriteList)//(favoriteList )
        favAdapter.notifyDataSetChanged()
        binding.favRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.favRecyclerView.hasFixedSize()
        binding.favRecyclerView.setItemViewCacheSize(13)
       binding.favRecyclerView.adapter=favAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favAdapter.setOnItemClickListner(object :FavoriteAdapter.onItemClickListner{
            override fun onItemClick(position: Int) { musicServices!!.setSongList(favoriteList,position)

             // Toast.makeText(context,"item ${position} clicked",Toast.LENGTH_SHORT).show()
            }

            override fun onOptionClick(position: Int) {
                // to remove song from favorite list
                popupMenu(position)
            }
        })
//        binding.shuffleBt.setOnClickListener{
//
//           //musicServices!!.musiclistSe.shuffle()
//                favoriteList.shuffle()
//               // isShuffle=true
//                Toast.makeText(context,"shuffle on",Toast.LENGTH_SHORT).show()
//        }

    }

    private fun popupMenu(position:Int){

        val popupMenu= PopupMenu(context,view)
        popupMenu.inflate(R.menu.fav_option_menu)
        popupMenu.setOnMenuItemClickListener (object :PopupMenu.OnMenuItemClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.removeFromPlayList->{
                        //*****************
                        //function to remove item from database
                          val tempMusic= favoriteList[position]
                              removeSongFromFavorite(position)
                        favoriteList.remove(tempMusic)

                        favAdapter.notifyDataSetChanged()
                     return   true
                    }
                    R.id.cancel_action ->{
                        popupMenu.dismiss()
                        return true
                    }
                    else ->
                        return false
                }

            }

        })

        popupMenu.show()
    }

    private fun removeSongFromFavorite(position: Int){

        val selectedSong=favoriteList[position]


        GlobalScope.launch (Dispatchers.IO){ musicServices!!.favMusicDa.deleteSong(selectedSong) }

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()
       musicServices!!.favoritelistSe=favoriteList
    //    Toast.makeText(context,"Favorite connected",Toast.LENGTH_SHORT).show()


    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }


}