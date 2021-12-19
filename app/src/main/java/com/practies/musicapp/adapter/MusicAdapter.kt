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
import com.practies.musicapp.model.Music
import com.practies.musicapp.R


//private val context: Context    , private val intemOnclicked:(Music) ->Unit  ,var onSongSelect: onSongSelect
class MusicAdapter(private val musicList: ArrayList<Music>) :RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    private lateinit var mListenr:onItemClickListener
   // lateinit var  mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
        fun onOptionClick(position: Int)
    }

   fun setOnItemClickListener(listener:onItemClickListener){
                mListenr=listener
   }

    class MyHolder(itemView:View ,listener: onItemClickListener):RecyclerView.ViewHolder(itemView)
    {
        val title= itemView.findViewById<TextView>(R.id.songs_nameMv)

       // val duration=itemView.findViewById<TextView>(R.id.song_duration)
        val albumName=itemView.findViewById<TextView>(R.id.song_album)
        val songImage= itemView.findViewById<ImageView>(R.id.imageMv)
        val optionMenu=itemView.findViewById<ImageButton>(R.id.option_icon)

           init {
              itemView.setOnClickListener {
                  listener.onItemClick(adapterPosition)
              }
               optionMenu.setOnClickListener{
                   listener.onOptionClick(adapterPosition)            //onItemClick(position)
               }


           }




    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MyHolder {

        val itemview=LayoutInflater.from(parent.context).inflate(R.layout.music_view,parent,false)

             return MyHolder(itemview,mListenr)//,mListenr
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
       val item=musicList [position]
        holder.title.text=item.title
        holder.albumName.text=item.album
        //holder.duration.text= formatDuration(item.duration)

        Glide.with(holder.itemView.context).load(musicList[position].artUri)
            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
            .into(holder.songImage)


    }


    override fun getItemCount(): Int {
      return musicList.size
    }



}
