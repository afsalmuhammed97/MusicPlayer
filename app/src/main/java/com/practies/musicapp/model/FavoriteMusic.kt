package com.practies.musicapp.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorites")
   //@Parcelize
data class FavoriteMusic (
     @PrimaryKey                           //(autoGenerate = true)
     val id: String,       //val uid: Int,
     val title:String?,
     val album:String?,
     val artist:String?,
     val duration:Long =0,
     val path:String?,
     val artUri:String?,
     val playListId:Int=0 )  //:ASong(id,title,album,artist,duration,path,artUri,playListId),


