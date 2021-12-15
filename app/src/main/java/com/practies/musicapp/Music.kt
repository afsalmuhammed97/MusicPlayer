package com.practies.musicapp

import com.practies.musicapp.database.FavoriteDataBase
import com.practies.musicapp.database.MusicDao
import com.practies.musicapp.databinding.FavoriteViewBinding
import com.practies.musicapp.fragments.favoriteFragment
import com.practies.musicapp.service.MusicServices
import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

data class Music(
    val id: String,
    val title:String,
    val album:String,
    val artist:String, val duration:Long =0,
    val path:String, val artUri:String ,val playListId:Int=0 ): Serializable
    //:Serializable
fun formatDuration(duration: Long):String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

//var musicServices:WeakReference<MusicServices>?=null

    //(context)?.userDao()!!



