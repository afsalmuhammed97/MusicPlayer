package com.practies.musicapp.musicDatabase

import androidx.room.*
import com.practies.musicapp.model.Music


@Dao
interface MusicDao {

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    fun addSong(music: Music)

    @Query("SELECT * FROM allMusics WHERE play_list_name = 'favorites'")
   fun readAllFavoriteSong():List<Music>
    // to get the names of play list
     @Query("SELECT DISTINCT play_list_name FROM allMusics ")
     fun getAllPlayListName():List<String>
//
     @Query("SELECT * FROM allMusics WHERE play_list_name LIKE :playName")
     fun getPlayList(playName:String):List<Music>

      // to check the song is exist or not in the playList
     @Query("SELECT * FROM allMusics WHERE id LIKE :song_id AND play_list_name LIKE:name")
       fun checkSongExist(song_id:String,name:String):Boolean
     // fun deletePlayList()
     @Query("DELETE  FROM allMusics WHERE play_list_name LIKE :playListName")
        fun deletePlayList(playListName:String)



    @Delete
    fun deleteSong(music: Music)
     @Query("SELECT * FROM  allMusics  ")
     fun readAllSongs():List<Music>


}