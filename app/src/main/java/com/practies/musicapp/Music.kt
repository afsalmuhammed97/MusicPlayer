package com.practies.musicapp

import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import androidx.room.ColumnInfo
import java.io.Serializable
import java.util.concurrent.TimeUnit

//:Serializable


const val favorite="favorites"

fun formatDuration(duration: Long):String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

var playList=ArrayList<String>()

    fun getImageArt(path:String): ByteArray? {
        val retriever=MediaMetadataRetriever()
        retriever.setDataSource(path)
        return  retriever.embeddedPicture
    }



