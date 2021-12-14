package com.practies.musicapp.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorites")
   @Parcelize
data class FavoriteMusic (
                               //(autoGenerate = true)
     @PrimaryKey     val id: String,       //val uid: Int,
     @ColumnInfo    val title:String?,
     @ColumnInfo  val album:String?,
     @ColumnInfo val artist:String?,
     @ColumnInfo  val duration:Long =0,
     @ColumnInfo  val path:String?,
     @ColumnInfo  val artUri:String?,
     @ColumnInfo  val playListId:Int=0 ):Parcelable  //:ASong(id,title,album,artist,duration,path,artUri,playListId),


