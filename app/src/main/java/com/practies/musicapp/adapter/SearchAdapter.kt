package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.R
import com.practies.musicapp.model.Music

class SearchAdapter (private val songList:ArrayList<Music>):RecyclerView.Adapter<SearchAdapter.MyHolder>(){
    class MyHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val songName=itemView.findViewById<TextView>(R.id.songs_name)
        val songImage=itemView.findViewById<ImageView>(R.id.image)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
val itemView=LayoutInflater.from(parent.context).inflate(R.layout.search_list_view,parent,false)
        return  MyHolder(itemView)
    }

    override fun onBindViewHolder(holder:MyHolder, position: Int) {
        val item= songList[position]
        holder.songName.text=item.title
        Glide.with(holder.itemView.context).load(songList[position].artUri)
            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
            .into(holder.songImage)



    }

    override fun getItemCount(): Int {
       return  songList.size
    }
}