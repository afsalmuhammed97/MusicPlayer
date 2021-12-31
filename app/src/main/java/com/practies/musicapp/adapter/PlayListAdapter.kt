package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practies.musicapp.R

class PlayListAdapter (private val playList: ArrayList<String>) :RecyclerView.Adapter<PlayListAdapter.PlayListHolder>() {

    private lateinit var pListener: MusicAdapter.onItemClickListener

    fun setOnItemClickListner(listener: MusicAdapter.onItemClickListener){
        pListener=listener
    }





    class PlayListHolder(itemview: View,listener: MusicAdapter.onItemClickListener):RecyclerView.ViewHolder(itemview) {
        val playlistName=itemview.findViewById<TextView>(R.id.play_list_name_1)
     //  val playListImage=itemview.findViewById<ImageView>(R.id.play_list_image_1)
        val deleteBt=itemview.findViewById<ImageButton>(R.id.delete_bt_1)
        init {

            itemview.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }

            deleteBt.setOnClickListener{
                listener.onOptionClick(absoluteAdapterPosition,itemview)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListHolder {

        val itemview=LayoutInflater.from(parent.context).inflate(R.layout.play_list_1,parent,false)

        return PlayListHolder(itemview,pListener)
    }

    override fun onBindViewHolder(holder: PlayListHolder, position: Int) {
          val item=playList[position]
        holder.playlistName.text=item




    }

    override fun getItemCount(): Int {
      return  playList.size
    }
}