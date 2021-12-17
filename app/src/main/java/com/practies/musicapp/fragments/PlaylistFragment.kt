package com.practies.musicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.R
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.adapter.PlayListAdapter
import com.practies.musicapp.databinding.FragmentPlaylistBinding


class PlaylistFragment : Fragment() {
lateinit var binding: FragmentPlaylistBinding
private lateinit var adapter:PlayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       binding=FragmentPlaylistBinding.inflate(inflater,container,false)

        val list=ArrayList<String> ()
        list.add("List 1")
        list.add("List 2")
        list.add("List 3")
        list.add("List 4")
        list.add("List 5")
        list.add("List 6")
        list.add("List 7")



        adapter= PlayListAdapter(list)
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
              Toast.makeText(context,"item clicked ${position}",Toast.LENGTH_SHORT).show()
            }

            override fun onOptionClick(position: Int) {
                Toast.makeText(context,"delete clicked ${position}",Toast.LENGTH_SHORT).show()
            }

        })

    }


//    //    private lateinit var musicAdapter: MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }












}
