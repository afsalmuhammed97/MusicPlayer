package com.practies.musicapp.fragments


import android.annotation.SuppressLint
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
import com.practies.musicapp.R
import com.practies.musicapp.activities.PlayScreenActivity
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.adapter.PlayListNameAdapter
import com.practies.musicapp.databinding.FragmentAlSongsBinding
import com.practies.musicapp.model.model2.Music
import com.practies.musicapp.model.model2.mediaStatus
import com.practies.musicapp.playList
import com.practies.musicapp.service.MusicServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class AlSongsFragment (): Fragment(),ServiceConnection{
    var  musicServices: MusicServices?=null
    lateinit var binding: FragmentAlSongsBinding
    private  lateinit var adapter:MusicAdapter
   lateinit var listAdapter:PlayListNameAdapter
   var  musiclist= arrayListOf<Music>()
    var play=ArrayList<Music>()
    var existSong:Boolean=false


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




    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding= FragmentAlSongsBinding.inflate(inflater,container,false)
         adapter = MusicAdapter()
        adapter.differ.submitList(musiclist)
        adapter.notifyDataSetChanged()
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
                val intent=Intent(context, PlayScreenActivity::class.java)
                intent.putExtra("music",musiclist[position])
                startActivity(intent)

            }

            override fun onOptionClick(position: Int, itemview: View) {

               customAlertDialog(position)
                    //  createDialog()
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
         val finel=  builder.show()

    listAdapter = PlayListNameAdapter(playList)
    listAdapter.notifyDataSetChanged()
    listRecycle.layoutManager = LinearLayoutManager(context)
    listRecycle.adapter = listAdapter
    createBt.setOnClickListener {
        createDialog()
        // to create new play list
    }
    var songExist=false

    listAdapter.setOnItemClickListener(object : MusicAdapter.onItemClickListener {

//   to add selected song into playlist
        override fun onItemClick(position: Int) {

            val tempPlayListName= playList[position]

            //Log.i("DB Check list Name",tempPlayListName)

            val song:Music
            song=musiclist[songPostion]


           // Log.i("D check selected song",song.toString())


                song.timeStamp=System.currentTimeMillis().toString()+song.id

                song.play_list_name= tempPlayListName

                GlobalScope.launch (Dispatchers.IO){

                        songExist = musicServices!!.favMusicDa.checkSongExist(song.id, tempPlayListName)


                        Log.i("Song Exist", if (songExist) "true" else "false")

                        if (!songExist) {
                            musicServices!!.favMusicDa.addSong(song)

                            play = musicServices!!.favMusicDa.getPlayList(tempPlayListName) as ArrayList<Music>

                        }



                  finel.dismiss()

                        //   Log.i("PLY List",play.toString())

            }
            if ( !songExist){ Toast.makeText(context,"One song added to playLis ",Toast.LENGTH_SHORT).show()   }

        }



        override fun onOptionClick(position: Int, itemview: View) {
            TODO("Not yet implemented")
        }

    })







    }






@SuppressLint("Recycle", "Range")
private fun  getAllAudio():ArrayList<Music>{
    val tempList=ArrayList<Music>()
    //to fetch the music
    val selection = MediaStore.Audio.Media.IS_MUSIC +"!= 0"
    val projection= arrayOf(
        MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)


    //MediaStore.Audio.Media.DATE_ADDED +" DESC"
        val cursor:Cursor?=activity?.contentResolver?.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,
            selection,null,
            MediaStore.Audio.Media.DURATION + ">= 60000",null
        )
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
        if (! musicServices!!.mediaPlayer.isPlaying && ! mediaStatus && musicServices!!.lastSong !=null){  musicServices!!.setInitialView(musiclist)  }

        Log.i("MSG","service connected")





    }

    override fun onServiceDisconnected(name: ComponentName?) {
              musicServices=null

    }


}


