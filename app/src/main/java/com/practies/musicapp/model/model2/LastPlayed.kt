package com.practies.musicapp.model.model2

import java.io.Serializable

data class LastPlayed(
    var timeStamp:String,
    val id: String,
    val title:String,
    val album:String,
    val artist:String,
    val duration:Long =0,
    val path:String,
    val artUri:String,
    var play_list_name:String,
    val songIndex:Int): Serializable

  var mediaStatus:Boolean=false

