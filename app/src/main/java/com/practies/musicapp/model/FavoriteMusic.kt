package com.practies.musicapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMusic (
    @PrimaryKey(autoGenerate = true) //val uid: Int,
    val id: String,
    val title:String,
    val album:String,
    val artist:String,
    val duration:Long =0,
    val path:String,
    val artUri:String ,
    val playListId:Int=0 ) //:Parcelable

