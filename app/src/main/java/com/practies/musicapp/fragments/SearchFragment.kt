package com.practies.musicapp.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.R
import com.practies.musicapp.adapter.SearchAdapter
import com.practies.musicapp.adapter.SongListAdapter
import com.practies.musicapp.databinding.FragmentSearchBinding
import com.practies.musicapp.model.model2.Music
import com.practies.musicapp.model.model2.musicServices
import com.practies.musicapp.service.MusicServices
import java.util.*


class SearchFragment : Fragment(),ServiceConnection {
    lateinit var binding: FragmentSearchBinding
//    lateinit var searchAdapter:SongListAdapter
//     var searchAdapter1:SearchAdapter?=null
//    var musicServices:MusicServices?=null
//    val songList= arrayListOf<Music>()
//    val searchList= arrayListOf<Music>()
//    val tempList= arrayListOf<String>()
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val intent= Intent(context, MusicServices::class.java)
        requireActivity().bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
        requireActivity().startService(intent)






    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= FragmentSearchBinding.inflate(inflater,container,false)




        binding.searchListRv.apply {
           // adapter=searchAdapter1
//            searchAdapter1!!.notifyDataSetChanged()

            layoutManager= LinearLayoutManager(context)
            hasFixedSize()
            setItemViewCacheSize(13)

        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        binding.searchText.addTextChangedListener(object :TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////
////                val searchInput=s.toString().lowercase(Locale.getDefault())
//              //  Toast.makeText(context,s,Toast.LENGTH_SHORT).show()
//                for (song in songList){
//                    if (song.title.lowercase().contains(searchInput)){
//                        searchList.add(song)
//                          searchAdapter= SongListAdapter(searchList)
//                        searchAdapter.notifyDataSetChanged()
//
//                        showSearch()
//                    }
//                }
//         }
//
//            override fun afterTextChanged(s: Editable?) {
//
//                searchFilter(s.toString())
//            }
//
//        })
//
//    }

//    private fun searchFilter(searchInput: String) {
//               val filterList= arrayListOf<Music>()
//
//        for (song in songList) {
//            if (song.title.lowercase().contains(searchInput.lowercase())) {
//                searchList.add(song)
//            }
//
//        }
//
//    }
//    @SuppressLint("NotifyDataSetChanged")
//    private fun showSearch(){
//        //searchAdapter= SongListAdapter(songList)
////        binding.searchListRv.apply {
////            adapter=searchAdapter
////            searchAdapter.notifyDataSetChanged()
//
//            layoutManager= LinearLayoutManager(context)
//            hasFixedSize()
//            setItemViewCacheSize(13)
//
//        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as MusicServices.Mybinder


    }

    override fun onServiceDisconnected(name: ComponentName?) {}





    }

//    searchText.addTextChangedListener(object :TextWatcher{
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            // Toast.makeText(this@MainActivity,s,Toast.LENGTH_SHORT).show()
//            if(s != null){
//                val userInput= s.toString().lowercase(Locale.getDefault())
//                for (song  in fragment.musiclist){
//                    if (song.title.lowercase().contains(userInput)){
//                        searchList.add(song)
//
//                    }
//                }
//            }
//
//        }
//





