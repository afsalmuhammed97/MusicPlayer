package com.practies.musicapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.MainActivity
import com.practies.musicapp.R
import com.practies.musicapp.SongListActivity2
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.adapter.PlayListAdapter
import com.practies.musicapp.databinding.FragmentPlaylistBinding
import com.practies.musicapp.model.Music
import com.practies.musicapp.musicDatabase.MusicDao
import com.practies.musicapp.musicDatabase.MusicDatabase
import com.practies.musicapp.playList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PlaylistFragment : Fragment() {
lateinit var binding: FragmentPlaylistBinding
private lateinit var adapter:PlayListAdapter
 lateinit var  musicDa:MusicDao
    var songList=ArrayList<Music>()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       binding=FragmentPlaylistBinding.inflate(inflater,container,false)

        musicDa= MusicDatabase.getDatabase(requireContext()).musicDao()


        adapter= PlayListAdapter(playList)
        adapter.notifyDataSetChanged()
       binding.playListRv.layoutManager=LinearLayoutManager(context)

        binding.playListRv.hasFixedSize()
        binding.playListRv.setItemViewCacheSize(13)
        binding.playListRv.adapter=adapter

        return  binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setOnItemClickListner(object :PlayListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val name= playList[position]
                songList=loadSongList(name)
                   Log.i("SongList",songList.toString())
                val intent=Intent(context,SongListActivity2::class.java)
             //  intent.putExtra("songs",)
                startActivity(intent)

              Toast.makeText(context,"item clicked ${position}",Toast.LENGTH_SHORT).show()

            }

            override fun onOptionClick(position: Int) {
                Toast.makeText(context,"delete clicked ${position}",Toast.LENGTH_SHORT).show()
            }

        })

    }
        //to get the selected song list
    fun loadSongList(listName:String):ArrayList<Music>{
        var List=ArrayList<Music>()
        GlobalScope.launch (Dispatchers.IO){
            List=musicDa.getPlayList(listName) as ArrayList<Music>
            Log.i("SongList",List.toString())
        }
        return List
    }

    fun loadFragment(){
        val transaction= activity?.supportFragmentManager?.beginTransaction()
           transaction?.replace(R.id.view_pager2,SongListFragment())
            transaction?.disallowAddToBackStack()
         transaction?.commit()

    }

//    //    private lateinit var musicAdapter: MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }












}
