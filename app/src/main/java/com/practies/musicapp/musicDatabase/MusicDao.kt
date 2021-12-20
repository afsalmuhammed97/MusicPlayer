package com.practies.musicapp.musicDatabase

import androidx.room.*
import com.practies.musicapp.model.Music


@Dao
interface MusicDao {

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    fun addSong(music: Music)

    @Query("SELECT * FROM allMusics WHERE play_list_name = 'favorites'")
   fun readAllFavoriteSong():List<Music>

     @Query("SELECT DISTINCT play_list_name FROM allMusics ")
     fun getAllPlayListName():List<String>
// to get the names of play list
     @Query("SELECT * FROM allMusics WHERE play_list_name LIKE :playName")
     fun getPlayList(playName:String):List<Music>

      //
     // fun deletePlayList()

    @Delete
    fun deleteSong(music: Music)
     @Query("SELECT * FROM  allMusics  ")
     fun readAllSongs():List<Music>


}