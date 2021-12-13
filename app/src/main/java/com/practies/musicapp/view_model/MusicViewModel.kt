package com.practies.musicapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.practies.musicapp.Music
import com.practies.musicapp.database.FavoriteDataBase
import com.practies.musicapp.database.FavoriteRepository
import com.practies.musicapp.model.FavoriteMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(application: Application):AndroidViewModel(application)  {

private val repository:FavoriteRepository
private val readAllFavSongs:MutableList<FavoriteMusic>


init {
    val musicDao=FavoriteDataBase.getDatabase(application).musicDao()
    repository=FavoriteRepository(musicDao)
      readAllFavSongs=repository.readAllSongs
}

      fun addSong(favoriteMusic:FavoriteMusic){
viewModelScope.launch (Dispatchers.IO){
    repository.addSong(favoriteMusic )
}

      }

}