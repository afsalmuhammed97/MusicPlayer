package com.practies.musicapp.musicDatabase

import androidx.room.*
import com.practies.musicapp.model.Music


@Dao
interface MusicDao {

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    fun addSong(music: Music)
   // @Query("SELECT * FROM `allMusics` WHERE `playlist-ame` LIKE 'favourites'")
    @Query("SELECT * FROM  allmusics   WHERE  playListName LIKE  'favorite'  ")
    fun readAllFavoriteSong(): List<Music>

    @Delete
    fun deleteSong(music: Music)
     @Query("SELECT * FROM  allMusics  ")
     fun readAllSongs():List<Music>


}