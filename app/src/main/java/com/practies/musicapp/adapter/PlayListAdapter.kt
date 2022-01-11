package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practies.musicapp.R
import com.practies.musicapp.databinding.PlayList1Binding

class PlayListAdapter (private val playList: ArrayList<String>) :RecyclerView.Adapter<PlayListAdapter.PlayListHolder>() {

    private lateinit var pListener: MusicAdapter.onItemClickListener

    fun setOnItemClickListner(listener: MusicAdapter.onItemClickListener){
        pListener=listener
    }





    class PlayListHolder(val binding: PlayList1Binding,listener: MusicAdapter.onItemClickListener):RecyclerView.ViewHolder(binding.root) {

//

        init {

            binding.root.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }

            binding.deleteBt1.setOnClickListener{
                listener.onOptionClick(absoluteAdapterPosition,binding.root)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListHolder {


        return PlayListHolder(PlayList1Binding.inflate(LayoutInflater.from(parent.context),parent,false),pListener)
    }

    override fun onBindViewHolder(holder: PlayListHolder, position: Int) {
          val item=playList[position]

          holder.binding.playListName1.text=item



    }

    override fun getItemCount(): Int {
      return  playList.size
    }
}