//package com.practies.musicapp.fragments
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Context
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.practies.musicapp.Music
//import com.practies.musicapp.R
//import com.practies.musicapp.adapter.MusicAdapter1
//import com.practies.musicapp.databinding.FragmentLibraryBinding
//import java.io.File
//
////class LibraryFragment : Fragment(R.layout.fragment_library) {
//
//    private var _binding: FragmentLibraryBinding? = null
//    private val binding get() = _binding!!
//    private var songList = arrayListOf<Music>()
//    private lateinit var musicAdapter:
//}
//
////
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
//        return binding.root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

//        songList=getAllAudio()
//        setRecyclerView()
//    }
//
//    private fun setRecyclerView(){
//        musicAdapter= MusicAdapter1()
//        binding.musicRV.apply {
//            adapter=musicAdapter
//
//            setHasFixedSize(true)
//        }
//        musicAdapter.differ.submitList(songList)
//        songList.clear()}




//    @SuppressLint("Range")
//    private fun  getAllAudio():ArrayList<Music>{
//        val tempList= arrayListOf<Music>()
//        //to fetch music files
//
//        val selection = MediaStore.Audio.Media.IS_MUSIC+"!=0"
//        val projection= arrayOf(
//            MediaStore.Audio.Media._ID,
//            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.ALBUM,
//            MediaStore.Audio.Media.ARTIST,
//            MediaStore.Audio.Media.DURATION,
//            MediaStore.Audio.Media.DATE_ADDED,
//            MediaStore.Audio.Media.DATA,
//            MediaStore.Audio.Media.ALBUM_ID)
//        val cursor= context?.contentResolver?.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,
//            MediaStore.Audio.Media.DATE_ADDED +" DESC",null)
//
//        if (cursor != null) {
//            if (cursor.moveToFirst())
//                do {
//                    val titleC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
//                    val idC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
//                    val albumC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
//                    val artistC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
//                    val pathC= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
//                    val durationC= cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
//                    val albumIdC =cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
//                    val uri = Uri.parse("content://media/external/audio/albumart")
//                    val artUriC= Uri.withAppendedPath(uri, albumIdC).toString()
//
//                    val music=Music(id=idC,title=titleC,album = albumC ,artist = artistC,path = pathC,duration = durationC,artUri =artUriC)
//                  //to convert song duration
//
//
//
//                    //to get path of song
//                    val file= File(music.path)
//                    if (file.exists())
//                        tempList.add(music)
//                } while (cursor.moveToNext())
//            cursor.close()
//        }
//        return tempList
//    }
//


//    override fun onDestroy() {
//        super.onDestroy()
//        _binding=null
//    }

//  old      val view =inflater.inflate(R.layout.fragment_library, container, false)
//        val recyclerView=view.findViewById<RecyclerView>(R.id.musicRV)
//

//        val musicList= ArrayList<String>()
//        musicList.add("perception")
//        musicList.add("Hard work")
//        musicList.add("Discipline")
//        musicList.add("Kotlin")
//        musicList.add("Python")
//        musicList.add("Jango")
//        musicList.add("Android")
//        musicList.add("Java")
//        musicList.add("Mearn")
//        musicList.add("java script")
//        musicList.add("Java")
//        musicList.add("React")
////        val adapter=MusicAdapter()
//        recyclerView.adapter=adapter
//         recyclerView.layoutManager=LinearLayoutManager(requireContext())

//        return view



//

