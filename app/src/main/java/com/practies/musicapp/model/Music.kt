package com.practies.musicapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practies.musicapp.service.MusicServices
import java.io.Serializable
import java.lang.ref.WeakReference


@Entity(tableName = "allMusics")

data class Music(
    @PrimaryKey
    var timeStamp:String,
    @ColumnInfo val id: String,
    @ColumnInfo val title:String,
    @ColumnInfo val album:String,
    @ColumnInfo val artist:String,
    @ColumnInfo val duration:Long =0,
    @ColumnInfo val path:String,
    @ColumnInfo val artUri:String,
    @ColumnInfo var play_list_name:String ): Serializable

var lastPlayedSongId:String=""
var musicServices: WeakReference<MusicServices>?=null