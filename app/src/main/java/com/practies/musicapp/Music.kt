package com.practies.musicapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.concurrent.TimeUnit

@Entity (tableName = "allMusics")

data class Music(
     @PrimaryKey  val id: String,
     @ColumnInfo val title:String,
     @ColumnInfo val album:String,
     @ColumnInfo val artist:String,
     @ColumnInfo val duration:Long =0,
     @ColumnInfo val path:String,
     @ColumnInfo val artUri:String,
     @ColumnInfo val playListId:Int=0 ): Serializable
    //:Serializable


const val favorite="favorites"

fun formatDuration(duration: Long):String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }




//var musicServices:WeakReference<MusicServices>?=null

    //(context)?.userDao()!!



