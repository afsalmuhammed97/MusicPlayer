package com.practies.musicapp

import kotlin.properties.Delegates

data class PlayList (
    val playListName:String,
    val songCount:Int ,val playlistImage:String)


class   PlayListDemo{

    lateinit var name:String
    lateinit var newPlayList:ArrayList<Music>
    var playlistId by Delegates.notNull<Int>()

}

class MusicPlayList{
    var  reference:ArrayList<PlayList>  =ArrayList()
}