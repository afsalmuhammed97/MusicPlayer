package com.practies.musicapp.database

import androidx.lifecycle.LiveData
import com.practies.musicapp.model.FavoriteMusic

class FavoriteRepository (private  val musicDao: MusicDao)  {

    val readAllSongs: MutableList<FavoriteMusic> = musicDao.readAllSongs()


    suspend fun  addSong(favoriteMusic: FavoriteMusic){
        musicDao.addSong(favoriteMusic)
    }






}