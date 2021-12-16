package com.practies.musicapp.musicDatabase

import androidx.room.*
import com.practies.musicapp.Music
@Dao
interface MusicDao {

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    fun addSong(music: Music)

    @Query("SELECT * FROM  allmusics    WHERE  playListId=0 ORDER BY  title")
    fun readAllFavoriteSong(): List<Music>

    @Delete
    fun deleteSong(music: Music)



}