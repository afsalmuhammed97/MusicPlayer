package com.practies.musicapp.fragments


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practies.musicapp.BuildConfig.DEBUG
import com.practies.musicapp.Music
import com.practies.musicapp.PlayScreenActivity
import com.practies.musicapp.adapter.MusicAdapter
import com.practies.musicapp.databinding.FragmentAlSongsBinding
import com.practies.musicapp.service.MusicServices
import com.practies.musicapp.view_model.MusicViewModel
import org.greenrobot.eventbus.EventBus
import java.io.File
//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888
//,ServiceConnection
class AlSongsFragment (): Fragment(),ServiceConnection{
       var  musicServices: MusicServices?=null
    lateinit var binding: FragmentAlSongsBinding
    var servicesIntent :Intent?=null
var songPosition=0
     val i:String=""

private  lateinit var adapter:MusicAdapter


      var  musiclist= arrayListOf<Music>()

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

       // Toast.makeText(context,"service started ",Toast.LENGTH_SHORT).show()





    }


    override fun onDestroy() {


        super.onDestroy()
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
               // musicServices!!.musiclistSe=musiclist
              //  musicServices!!.initMediaPlayer()
              //  musicServices!!.playSong()
               // val song=musiclist[position]
              //  EventBus.getDefault().post(song)
            //    Log.i("TAG",musicServices?.musiclistSe.toString())
                val intent=Intent(context,PlayScreenActivity::class.java)
                startActivity(intent)



            }

        })

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

                val music=Music(
                    id =idC,
                    title =titleC,
                    album = albumC ,
                    artist = artistC,
                    path = pathC,
                    duration = durationC,
                    artUri =artUriC)
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
           Toast.makeText(context,"service connected",Toast.LENGTH_SHORT).show()


    // Log.d("TAG","music service connected")
    // musicServices!!.musiclistSe=musiclist
//     Log.i("TAG",musicServices?.musiclistSe.toString())
    // musicServices!!.songPosition=songPosition
    }

    override fun onServiceDisconnected(name: ComponentName?) {

    }


}


