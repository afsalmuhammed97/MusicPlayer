package com.practies.musicapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.SongListActivity2
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.adapter.PlayListAdapter
import com.practies.musicapp.databinding.FragmentPlaylistBinding
import com.practies.musicapp.model.Music
import com.practies.musicapp.musicDatabase.MusicDao
import com.practies.musicapp.musicDatabase.MusicDatabase
import com.practies.musicapp.playList
import kotlinx.coroutines.*


class PlaylistFragment : Fragment() {
lateinit var binding: FragmentPlaylistBinding
private lateinit var adapter:PlayListAdapter
 lateinit var  musicDa:MusicDao
    var songList=ArrayList<Music>()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
       binding=FragmentPlaylistBinding.inflate(inflater,container,false)

        musicDa= MusicDatabase.getDatabase(requireContext()).musicDao()

        adapter= PlayListAdapter(playList)
        binding.playListRv.layoutManager=LinearLayoutManager(context)
        binding.playListRv.hasFixedSize()
        binding.playListRv.setItemViewCacheSize(13)
        binding.playListRv.adapter=adapter

        return  binding.root


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setOnItemClickListner(object : MusicAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val name= playList[position]
                loadSongList(name)



             // Toast.makeText(context,"item clicked ${playList[position]}",Toast.LENGTH_SHORT).show()

            }

            override fun onOptionClick(position: Int, itemview: View) {
                     deletePlayList(position)
            }

        })

    }
        //to delete the selected list

        fun deletePlayList(position:Int){
            val alertDialog=AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Delete PlayList")
            alertDialog.setMessage("Are you shure ,want to delete")
            alertDialog.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }
            alertDialog.setPositiveButton("Yes"){ _:DialogInterface,i:Int->

                val name= playList[position]
                GlobalScope.launch (Dispatchers.IO){ musicDa.deletePlayList(name)  }
                Toast.makeText(context,"${name}  deleted",Toast.LENGTH_SHORT).show()
                     deletePlayListView(position)
            }
           alertDialog.create()
            alertDialog.show()

    }
    //delete playList from view
    @SuppressLint("NotifyDataSetChanged")
    fun deletePlayListView(position: Int){
        val listElement= playList[position]
        playList.remove(listElement)
        adapter.notifyDataSetChanged()
    }

        //to get the selected song list

        fun loadSongList(listName:String){
      //  var List=ArrayList<Music>()

        GlobalScope.launch (Dispatchers.IO){
            withContext(Dispatchers.IO){
                songList=musicDa.getPlayList(listName) as ArrayList<Music>
                Log.i("SongList",songList.toString())

                withContext(Dispatchers.IO){


                    Log.i("SongList in view",songList.toString())
                    val intent=Intent(context,SongListActivity2::class.java)
                    intent.putExtra("songs",songList)
                    intent.putExtra("listName",listName)
                    Log.i("Test", playList.toString())
                    startActivity(intent)


                }

            }

        }

    }













}
