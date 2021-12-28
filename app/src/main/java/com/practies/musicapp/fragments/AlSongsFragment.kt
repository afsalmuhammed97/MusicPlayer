package com.practies.musicapp.fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.practies.musicapp.model.Music
import com.practies.musicapp.PlayScreenActivity
import com.practies.musicapp.R
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.adapter.PlayListNameAdapter
import com.practies.musicapp.databinding.FragmentAlSongsBinding
import com.practies.musicapp.model.mediaStatus
import com.practies.musicapp.playList
import com.practies.musicapp.service.MusicServices
import kotlinx.android.synthetic.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888

class AlSongsFragment (): Fragment(),ServiceConnection{
       var  musicServices: MusicServices?=null
    lateinit var binding: FragmentAlSongsBinding

      var existSong:Boolean=false

    private  lateinit var adapter:MusicAdapter
lateinit var listAdapter:PlayListNameAdapter
      var  musiclist= arrayListOf<Music>()
     var play=ArrayList<Music>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


           Log.i("Main","view model intialized")
         musiclist=getAllAudio()

        //to start service
        val intent =Intent(context, MusicServices::class.java)

         //// need  foreground service call ***************************

      requireActivity().bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
       requireActivity().startService(intent)

       // requireActivity().startService(Intent(context,MusicServices::class.java))






        Log.i("InAllSong","initialMethod called")


    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentAlSongsBinding.inflate(inflater,container,false)
         adapter = MusicAdapter(musiclist)
        binding.musicRV.layoutManager=LinearLayoutManager(context)
        binding.musicRV.hasFixedSize()
        binding.musicRV.setItemViewCacheSize(13)
        binding.musicRV.adapter=  adapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter.setOnItemClickListener( object :MusicAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {


                musicServices!!.setSongList(musiclist,position)
                val intent=Intent(context,PlayScreenActivity::class.java)
                intent.putExtra("music",musiclist[position])
                startActivity(intent)

            }

            override fun onOptionClick(position: Int) {
                       //  popupMenus(position)
                customAlertDialog(position)
                      // createDialog()
            }

        })

    }

        fun createDialog() {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            val customAlert=LayoutInflater.from(context).inflate(R.layout.create_list,binding.root,false)
            val playListTx=customAlert.findViewById<TextInputEditText>(R.id.play_list_text)
           alertDialogBuilder.setView(customAlert)

            alertDialogBuilder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }
            alertDialogBuilder.setPositiveButton("create") { _: DialogInterface, i: Int ->
                val play = playListTx.text
                playList.add(play.toString())
            }
            alertDialogBuilder.create()
            alertDialogBuilder.show()
        }





//  to show the popup window
@SuppressLint("NotifyDataSetChanged")
fun customAlertDialog(position: Int) {
//position for operation with database
        var songPostion=position

    val customDialog = LayoutInflater.from(context).inflate(R.layout.play_list_menu, binding.root, false)
    val createBt = customDialog.findViewById<Button>(R.id.create_bt)
    val listRecycle = customDialog.findViewById<RecyclerView>(R.id.playListRv)
    val builder = MaterialAlertDialogBuilder(requireContext())
    builder.setView(customDialog  )

    listAdapter = PlayListNameAdapter(playList)
    listAdapter.notifyDataSetChanged()
    listRecycle.layoutManager = LinearLayoutManager(context)
    listRecycle.adapter = listAdapter
    createBt.setOnClickListener {
        createDialog()
        // Toast.makeText(context,"crete bt clicked ",Toast.LENGTH_SHORT).show()

    }


    listAdapter.setOnItemClickListener(object : MusicAdapter.onItemClickListener {


        override fun onItemClick(position: Int) {
            //*****************************************
            val tempPlayListName= playList[position]
            val song:Music
            song=musiclist[songPostion]

            if ( ! checkSongInPlayList(song,tempPlayListName)){

                song.timeStamp=System.currentTimeMillis().toString()+song.id
                song.play_list_name= tempPlayListName
                GlobalScope.launch (Dispatchers.IO){
                    musicServices!!.favMusicDa.addSong(song)
                    play=musicServices!!.favMusicDa.getPlayList(tempPlayListName) as ArrayList<Music>

                    Log.i("PLY List",play.toString())
                }
                Toast.makeText(context,"One song added to Playlist ",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"This song already exist ",Toast.LENGTH_SHORT).show()
            }

        }

        override fun onOptionClick(position: Int) {
            TODO("Not yet implemented")
        }

    })


      builder.setPositiveButton("Ok") { dialog, _ ->
           dialog.dismiss()
       }
     builder.setNegativeButton("Cancel") { dialog, _ ->
           dialog.cancel()
       }
           builder.show()
    }

           //  to show the create playlist and ,  playlist names in a listView

fun checkSongInPlayList(song:Music,playListName:String):Boolean{
    existSong=false
    var customPlaylist=ArrayList<Music>()
    GlobalScope.launch (Dispatchers.IO) {
        customPlaylist = musicServices!!.favMusicDa.getPlayList(playListName) as ArrayList<Music>

    }
     customPlaylist.forEachIndexed{ _,mSong->
         if (song.id== mSong.id){
             existSong=true
             return true
         }

     }

        return false

}




////   val cursor:Cursor?=requireActivity().contentResolver.query(
@SuppressLint("Recycle", "Range")
private fun  getAllAudio():ArrayList<Music>{
    val tempList=ArrayList<Music>()
    //to fetch the music
    val selection = MediaStore.Audio.Media.IS_MUSIC +"!= 0"
    val projection= arrayOf(
        MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)
  //  requireActivity()


        val cursor:Cursor?=activity?.contentResolver?.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,
        MediaStore.Audio.Media.DATE_ADDED +" DESC",null)
    if (cursor != null) {
        if (cursor.moveToFirst())
            do {
                val titleC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val idC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val albumC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artistC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val pathC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val durationC= cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val albumIdC =cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                val uri = Uri.parse("content://media/external/audio/albumart")
                val artUriC= Uri.withAppendedPath(uri, albumIdC).toString()

                val music= Music(
                    id =idC,
                    title =titleC,
                    album = albumC ,
                    artist = artistC,
                    path = pathC,
                    duration = durationC,
                    artUri =artUriC,
                     timeStamp = "",
                       play_list_name = ""   )
                //to get path of song
                val file= File(music.path)
                if (file.exists())
                    tempList.add(music)
            } while (cursor.moveToNext())
        cursor.close()
    }
    return tempList
}

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
      val binder=service as MusicServices.Mybinder
        musicServices=binder.currentService()
        if (! musicServices!!.mediaPlayer.isPlaying && ! mediaStatus){  musicServices!!.setInitialView(musiclist)  }

        Log.i("MSG","service connected")

             //to get allPlayList from db
//        GlobalScope.launch (Dispatchers.IO){
//            playList=musicServices!!.favMusicDa.getAllPlayListName() as ArrayList<String>
//            musicServices!!.favoritelistSe=musicServices!!.favMusicDa.readAllFavoriteSong() as ArrayList<Music>
//         // Log.i("DB", playList.toString())
//        }



    }

    override fun onServiceDisconnected(name: ComponentName?) {
              musicServices=null

    }


}


