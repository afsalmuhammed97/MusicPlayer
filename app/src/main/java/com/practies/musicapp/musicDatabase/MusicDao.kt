package com.practies.musicapp.musicDatabase

import androidx.room.*
import com.practies.musicapp.Music
@Dao
interface MusicDao {

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    fun addSong(music: Music)

    @Query("SELECT * FROM  allmusics  ORDER BY  title")
    fun readAllSong(): List<Music>

    @Delete
    fun deleteSong(music: Music)


}