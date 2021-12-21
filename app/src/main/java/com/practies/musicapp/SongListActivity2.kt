package com.practies.musicapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.adapter.SongListAdapter
import com.practies.musicapp.databinding.ActivitySongList2Binding
import com.practies.musicapp.model.Music

class SongListActivity2 : AppCompatActivity() {
    var songList=ArrayList<Music>()
    lateinit var binding: ActivitySongList2Binding
    lateinit var songAdapter:SongListAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySongList2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
                val temp=ArrayList<String>()
             temp.add("song 1")
        temp.add("song 1")
        temp.add("song 2")
        temp.add("song 3")
        temp.add("song 4")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")
        temp.add("song 5")










        songAdapter= SongListAdapter(temp)
        songAdapter.notifyDataSetChanged()
        binding.songListRv.layoutManager=LinearLayoutManager(this)
        binding.songListRv.hasFixedSize()
        binding.songListRv.setItemViewCacheSize(10)
        binding.songListRv.adapter=songAdapter


    }
}