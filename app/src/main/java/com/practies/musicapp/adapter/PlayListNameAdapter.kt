package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practies.musicapp.R

class PlayListNameAdapter (private val playlistName:ArrayList<String>):RecyclerView.Adapter<PlayListNameAdapter.ListHolder>(){

   private lateinit var Listener: MusicAdapter.onItemClickListener
//    interface onItemClickListener{
//        fun onItemClick(position: Int)
//    }


   fun setOnItemClickListener(listener: MusicAdapter.onItemClickListener){
      Listener=listener
   }




    class ListHolder(view:View,listener: MusicAdapter.onItemClickListener):RecyclerView.ViewHolder(view) {
         val neme=view.findViewById<TextView>(R.id.list_name)
        init {
            view.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.play_list_name_view,parent,false)



        return ListHolder(itemView, Listener)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
                 val item=playlistName[position]
                holder.neme.text=item
    }

    override fun getItemCount(): Int {
       return  playlistName.size
    }


}