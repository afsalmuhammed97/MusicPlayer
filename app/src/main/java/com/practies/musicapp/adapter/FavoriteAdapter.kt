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
import com.practies.musicapp.Music
import com.practies.musicapp.R
import com.practies.musicapp.databinding.FragmentFavoriteBinding


class FavoriteAdapter(private val favoriteList:ArrayList<Music>):RecyclerView.Adapter<FavoriteAdapter.FavHolder>() {
    lateinit var fListener:onItemClickListner

  interface onItemClickListner{
       fun onItemClick(position: Int)
      fun onOptionClick(position: Int)
   }
    fun setOnItemClickListner(listener:onItemClickListner){
        fListener=listener
    }
  //  ,listener:onItemClickListner itemView:View
    class FavHolder(itemView:View,listener:onItemClickListner):RecyclerView.ViewHolder(itemView) {
           val title=itemView.findViewById<TextView>(R.id.songs_name_fav)
        val songImag=itemView.findViewById<ImageView>(R.id.imageMvFav)
        val options=itemView.findViewById<ImageButton>(R.id.option_icon_fav)
        init {
            itemView.setOnClickListener {
             listener.onItemClick(adapterPosition)
            }

            options.setOnClickListener{
                listener.onOptionClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.favorite_view,parent,false)
        return  FavHolder(itemView,fListener)//,fListener)
    }

    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val item=favoriteList[position]
        holder.title.text=item.title
       Glide.with(holder.itemView.context).load(favoriteList[position].artUri)
           .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
           .into(holder.songImag)


    }

    override fun getItemCount(): Int {
    return   favoriteList.size
    }
}