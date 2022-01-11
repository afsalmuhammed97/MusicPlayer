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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.R
import com.practies.musicapp.adapter.FavoriteAdapter
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.adapter.PlayListAdapter
import com.practies.musicapp.databinding.FragmentFavoriteBinding
import com.practies.musicapp.model.model2.Music
import com.practies.musicapp.musicDatabase.MusicDao
import com.practies.musicapp.musicDatabase.MusicDatabase.Companion.getDatabase
import com.practies.musicapp.playList
import com.practies.musicapp.service.MusicServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class favoriteFragment : Fragment(),ServiceConnection {
    lateinit var favMusicDao: MusicDao
     var musicServices:MusicServices?=null
    lateinit var binding: FragmentFavoriteBinding
   // lateinit var favoriteAdapter:MusicAdapter
        lateinit var favAdapter:FavoriteAdapter
    var  favoriteList= arrayListOf<Music>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Fav Frag", "favorite created")

        favMusicDao= getDatabase(requireContext()).musicDao()

        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                favoriteList = favMusicDao.readAllFavoriteSong() as ArrayList<Music>

                Log.i("Fav Frag", favoriteList.toString())
            }
        }

        val intent=Intent(context,MusicServices::class.java)
        requireActivity().bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
        requireActivity().startService(intent)
        // favMusicDao=getDatabase(requireActivity().application).musicDao()



    }












    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding= FragmentFavoriteBinding.inflate(inflater,container,false)

         setFavoriteView()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        favAdapter.notifyDataSetChanged()


  // favAdapter= FavoriteAdapter(favoriteList)

        super.onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPause() {
        super.onPause()


        favAdapter.notifyDataSetChanged()
}





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favAdapter.setOnItemClickListner(object :FavoriteAdapter.onItemClickListner{
            override fun onItemClick(position: Int) { musicServices!!.setSongList(favoriteList,position)

            }

            override fun onOptionClick(position: Int, view: View) {
                popupMenu(position,view)
            }


               // to remove song from favorite list


        },view)


    }

   @SuppressLint("NotifyDataSetChanged")
   fun setFavoriteView(){
       favAdapter= FavoriteAdapter(favoriteList)
       favAdapter.notifyDataSetChanged()
       binding.favRecyclerView.layoutManager=LinearLayoutManager(context)
       binding.favRecyclerView.hasFixedSize()
       binding.favRecyclerView.setItemViewCacheSize(13)
       binding.favRecyclerView.adapter=favAdapter


       }





    private fun popupMenu(position:Int,view: View){

        val popupMenu= PopupMenu(requireContext(),view)
        popupMenu.inflate(R.menu.fav_option_menu)
        popupMenu.setOnMenuItemClickListener (object :PopupMenu.OnMenuItemClickListener{

            @SuppressLint("NotifyDataSetChanged")
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.removeFromPlayList->{

                        removeSongFromFavorite(position)
                        favoriteList.remove(favoriteList[position])
                        favAdapter.notifyDataSetChanged()


                     return   true
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
//    fun songRemoveFromView(position: Int){
//        val element=songList[position]
//        // Toast.makeText(this,"song deleted ${element}",Toast.LENGTH_SHORT).show()
//        songList.remove(element)
//        songAdapter.notifyDataSetChanged()
//    }

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