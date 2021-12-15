package com.practies.musicapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.practies.musicapp.model.FavoriteMusic

@Dao
interface MusicDao {

     @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun  addSong(favoriteMusic: FavoriteMusic)


   @Query("SELECT * FROM  favorites ORDER BY title ")
   fun readAllSongs(): MutableList<FavoriteMusic>                   //LiveData<MutableList<FavoriteMusic>>
   // MutableList<FavoriteMusic>

   @Delete
   fun  deleteSong(favoriteMusic: FavoriteMusic)




}
