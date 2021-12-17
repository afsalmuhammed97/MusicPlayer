package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practies.musicapp.Music
import com.practies.musicapp.R

class PlayListAdapter (private val playList: ArrayList<String>) :RecyclerView.Adapter<PlayListAdapter.PlayListHolder>() {

    private lateinit var pListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
        fun onOptionClick(position: Int)
    }

    fun setOnItemClickListner(listener:onItemClickListener){
        pListener=listener
    }





    class PlayListHolder(itemview: View,listener:onItemClickListener):RecyclerView.ViewHolder(itemview) {
        val playlistName=itemview.findViewById<TextView>(R.id.play_list_name)
        val songCount=itemview.findViewById<TextView>(R.id.song_count)
        val playListImage=itemview.findViewById<ImageView>(R.id.play_list_image)
        val deleteBt=itemview.findViewById<ImageButton>(R.id.delete_bt)
        init {
            itemview.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }

            deleteBt.setOnClickListener{
                listener.onOptionClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListAdapter.PlayListHolder {

        val itemview=LayoutInflater.from(parent.context).inflate(R.layout.play_list_view,parent,false)

        return PlayListHolder(itemview,pListener)
    }

    override fun onBindViewHolder(holder: PlayListAdapter.PlayListHolder, position: Int) {
          val item=playList[position]
        holder.playlistName.text=item




    }

    override fun getItemCount(): Int {
      return  playList.size
    }
}