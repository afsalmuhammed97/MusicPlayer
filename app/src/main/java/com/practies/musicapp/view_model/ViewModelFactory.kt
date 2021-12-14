package com.practies.musicapp.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practies.musicapp.model.FavoriteMusic
import java.lang.IllegalArgumentException

class ViewModelFactory(private val  application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MusicViewModel::class.java) ){
            return  MusicViewModel(application  ) as T
        }
        throw  IllegalArgumentException("view not found")
    }
}