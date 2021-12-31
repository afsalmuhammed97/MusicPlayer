package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practies.musicapp.R
import com.practies.musicapp.model.Music

class SearchAdapter (private var songList:ArrayList<Music>):RecyclerView.Adapter<SearchAdapter.MyHolder>(){

    private lateinit var Listener:MusicAdapter.onItemClickListener

   fun setOnItemClickLisnter(listener:MusicAdapter.onItemClickListener){
       Listener=listener
   }



    class MyHolder(itemView:View,listener: MusicAdapter.onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val songName=itemView.findViewById<TextView>(R.id.songs_name)
        val songImage=itemView.findViewById<ImageView>(R.id.image)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
val itemView=LayoutInflater.from(parent.context).inflate(R.layout.serch_item_view,parent,false)
        return  MyHolder(itemView,Listener)
    }

    override fun onBindViewHolder(holder:MyHolder, position: Int) {
        val item= songList[position]
        holder.songName.text=item.title
//        Glide.with(holder.itemView.context).load(songList[position].artUri)
//            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
//            .into(holder.songImage)



    }

    override fun getItemCount(): Int {
       return  songList.size
    }
}