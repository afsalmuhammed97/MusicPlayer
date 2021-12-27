package com.practies.musicapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.adapter.PlayListAdapter
import com.practies.musicapp.adapter.SongListAdapter
import com.practies.musicapp.databinding.ActivitySongList2Binding
import com.practies.musicapp.model.Music
import com.practies.musicapp.service.MusicServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongListActivity2 : AppCompatActivity(),ServiceConnection {
    var songList=ArrayList<Music>()
    lateinit var binding: ActivitySongList2Binding
    var musicServices:MusicServices?=null
    var removed: Boolean=false
    lateinit var element :Music
    lateinit var songAdapter:SongListAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySongList2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        songList = intent.getSerializableExtra("songs") as ArrayList<Music>
        //intent.getSerializableExtra("songs")  as ArrayList<Music>
        val songListName=intent.getStringExtra("listName")
        Log.i("SongList", songList.toString())


        intent = Intent(this, MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)




        binding.playListName.text=songListName
        songAdapter = SongListAdapter(songList)
        binding.songListRv.layoutManager = LinearLayoutManager(this)
        binding.songListRv.hasFixedSize()
        binding.songListRv.setItemViewCacheSize(10)
        binding.songListRv.adapter = songAdapter


        songAdapter.setOnItemclickListner(object :SongListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                element=songList[position]
                deleteSelectedSong(position)






                }


            override fun onOptionClick(position: Int) {
                  musicServices!!.setSongList(songList,position)
                binding.songNameMini.text=songList[position].title }
        })


    }

    override fun onResume() {
        super.onResume()
    }
//    private fun popupMenu(position:Int,view: View){
//
//        val popupMenu= PopupMenu(this,view)
//        popupMenu.inflate(R.menu.fav_option_menu)
//        popupMenu.setOnMenuItemClickListener (object : PopupMenu.OnMenuItemClickListener{
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                when(item?.itemId){
//                    R.id.removeFromPlayList->{
//                        //*****************
//                        //function to remove item from database
//                        val song= songList[position]
//                        GlobalScope.launch(Dispatchers.IO) { musicServices!!.favMusicDa.deleteSong(song) }
//                        removeSongFromFavorite(position)
//                        favoriteList.remove(tempMusic)
//
//                        favAdapter.notifyDataSetChanged()
//                        return   true
//                    }
//                    R.id.cancel_action ->{
//                        popupMenu.dismiss()
//                        return true
//                    }
//                    else ->
//                        return false
//                }
//
//            }
//
//        })
//
//        popupMenu.show()
//    }










    fun deleteSelectedSong(position:Int):Boolean {

              val song = songList[position]
              val dialog = AlertDialog.Builder(this)
              dialog.setTitle("Delete Song")
              dialog.setMessage("Are you sure ,want to delete")
              dialog.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                  removed = false
                  dialogInterface.cancel()
              }
              dialog.setPositiveButton("Delete") { _: DialogInterface, i: Int ->
                  GlobalScope.launch(Dispatchers.IO) { musicServices!!.favMusicDa.deleteSong(song) }
                   //songList.remove(song)
                     songRemoveFromView(position)
                  removed = true
              }
              dialog.create()
              dialog.show()

              return removed
          }
    fun songRemoveFromView(position: Int){
        var element=songList[position]
       // Toast.makeText(this,"song deleted ${element}",Toast.LENGTH_SHORT).show()
        songList.remove(element)
        songAdapter.notifyDataSetChanged()
    }
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder= service as MusicServices.Mybinder
        musicServices=binder.currentService()
//        binding.songNameMini.text=songList[musicServices!!.currentIndex].title

        binding.playPauseMiniBt.setOnClickListener {  if (musicServices!!.mediaPlayer.isPlaying){

                musicServices!!.playPauseMusic(false)
                binding.playPauseMiniBt.setImageResource(R.drawable.play_bt_circle)

            }else{
                musicServices!!.playPauseMusic(true)
                binding.playPauseMiniBt.setImageResource(R.drawable.pause_bt_circle)

            } }
            binding.nextMiniBt.setOnClickListener {             musicServices!!.nextPreviousSong(true)
            }
            binding.prevMiniBt.setOnClickListener {     musicServices!!.nextPreviousSong(false) }

            binding.miniPlayerLayout.setOnClickListener {
                val intent=Intent(this,PlayScreenActivity::class.java)
                startActivity(intent)
            }
      //  Toast.makeText(this@SongListActivity2,"service connected",Toast.LENGTH_SHORT).show()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }
}