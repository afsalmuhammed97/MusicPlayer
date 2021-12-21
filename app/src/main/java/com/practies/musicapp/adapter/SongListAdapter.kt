package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.R
import com.practies.musicapp.model.Music

class SongListAdapter(private val songList:ArrayList<Music>):RecyclerView.Adapter<SongListAdapter.SongHolder>() {
       private lateinit var sListenr:MusicAdapter.onItemClickListener
    fun setOnItemclickListner( listener:MusicAdapter.onItemClickListener){
        sListenr=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListAdapter.SongHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.music_view,parent,false)
           return SongHolder(view)//sListenr

    }

    override fun onBindViewHolder(holder: SongListAdapter.SongHolder, position: Int) {
       val item=songList[position]
        holder.songNeme.text=  item.title
        holder.albumName.text= item.album
        Glide.with(holder.itemView.context).load(songList[position].artUri)
            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
            .into(holder.songImage)



    }

    override fun getItemCount(): Int {
       return songList.size
    }
   // ,listener:MusicAdapter.onItemClickListener
    class SongHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        val songNeme = itemView.findViewById<TextView>(R.id.songs_nameMv)
        // val duration=itemView.findViewById<TextView>(R.id.song_duration)
        val albumName=itemView.findViewById<TextView>(R.id.song_album)
        val songImage= itemView.findViewById<ImageView>(R.id.imageMv)
        val optionMenu=itemView.findViewById<ImageButton>(R.id.option_icon)

//        init {
//            itemView.setOnClickListener {
//                listener.onOptionClick(adapterPosition)
//            }
//            optionMenu.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
//        }

    }
}