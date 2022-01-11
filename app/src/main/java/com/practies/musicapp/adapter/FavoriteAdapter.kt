package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.R
import com.practies.musicapp.databinding.FavoriteViewBinding
import com.practies.musicapp.model.model2.Music

class FavoriteAdapter(private val favoriteList:ArrayList<Music>):RecyclerView.Adapter<FavoriteAdapter.FavHolder>(){
    lateinit var fListener:onItemClickListner

  interface onItemClickListner{
       fun onItemClick(position: Int)
      fun onOptionClick(position: Int,view: View)
   }
    
    fun setOnItemClickListner(listener:onItemClickListner,view: View) { fListener=listener }

    class FavHolder(val binding: FavoriteViewBinding,listener:onItemClickListner):RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
             listener.onItemClick(absoluteAdapterPosition)
            }

          binding.optionIconFav.setOnClickListener{
                listener.onOptionClick(absoluteAdapterPosition,itemView)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
        return  FavHolder(FavoriteViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),fListener)//,fListener)
    }

    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val item= favoriteList[position]
        holder.binding.songsNameFav.text=item.title

       Glide.with(holder.itemView.context).load(item.artUri)
           .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
           .into(holder.binding.imageMvFav)


    }

    override fun getItemCount()=favoriteList.size

}