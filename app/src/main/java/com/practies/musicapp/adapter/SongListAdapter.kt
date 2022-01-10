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
import com.practies.musicapp.databinding.MusicViewInListBinding
import com.practies.musicapp.model.model2.Music

class SongListAdapter(private val songList:ArrayList<Music>):RecyclerView.Adapter<SongListAdapter.SongHolder>() {
       private lateinit var sListenr:onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
        fun onOptionClick(position: Int)
    }
    fun setOnItemclickListner(listener:onItemClickListener){
        sListenr=listener
    }

    class SongHolder(val binding: MusicViewInListBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                listener.onOptionClick(absoluteAdapterPosition)
            }
           binding.deleteBt.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SongHolder {

           return SongHolder(MusicViewInListBinding.inflate(LayoutInflater.from(parent.context)
               ,parent,false),sListenr)

    }

    override fun onBindViewHolder(holder:SongHolder, position: Int) {
       val item=songList[position]
        holder.binding.songsNameMv.text=  item.title
        holder.binding.songAlbum.text= item.album
        Glide.with(holder.itemView.context).load(item.artUri)
            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
            .into(holder.binding.imageMv)



    }

    override fun getItemCount()=songList.size
   // ,listener:MusicAdapter.onItemClickListener
}