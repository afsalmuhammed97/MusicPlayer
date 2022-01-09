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
import com.practies.musicapp.databinding.MusicViewBinding
import com.practies.musicapp.model.model2.Music

//  private val musicList: ArrayList<Music>
//private val context: Context    , private val intemOnclicked:(Music) ->Unit  ,var onSongSelect: onSongSelect
class MusicAdapter() :RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    private lateinit var mListenr:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
        fun onOptionClick(position: Int, itemview: View)
    }

   fun setOnItemClickListener(listener:onItemClickListener){
                mListenr=listener
   }

    class MyHolder(val binding: MusicViewBinding ,listener: onItemClickListener):RecyclerView.ViewHolder(binding.root)
    {

           init { itemView.setOnClickListener {
                  listener.onItemClick(absoluteAdapterPosition) }


               binding.optionIcon.setOnClickListener{
                   listener.onOptionClick(absoluteAdapterPosition,itemView)          }


           }
    }

    private val diffCallback=object :DiffUtil.ItemCallback<Music>(){
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {

            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
           return newItem==oldItem
        }

    }
    val differ=AsyncListDiffer(this,diffCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyHolder {


             return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),mListenr)//,mListenr
    }


    override fun onBindViewHolder(holder: MyHolder, position: Int) {
       val item=differ.currentList[position]
          holder.binding.apply {
              songsNameMv.text=item.title
              songAlbum.text=item.album
          }

        Glide.with(holder.itemView.context).load(item.artUri)
            .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
            .into(holder.binding.imageMv)


    }


    override fun getItemCount()=differ.currentList.size



}
