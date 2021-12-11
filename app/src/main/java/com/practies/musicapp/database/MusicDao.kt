package com.practies.musicapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practies.musicapp.model.FavoriteMusic

@Dao
interface MusicDao {

     @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend  fun  addSong(favoriteMusic: FavoriteMusic)


   @Query("SELECT * FROM  favorites ORDER BY  title  ASC")
   fun readAllSongs():LiveData<List<FavoriteMusic>>
}