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

//private val favoriteList:ArrayList<Music>
class FavoriteAdapter():RecyclerView.Adapter<FavoriteAdapter.FavHolder>(){
    lateinit var fListener:onItemClickListner

  interface onItemClickListner{
       fun onItemClick(position: Int)
      fun onOptionClick(position: Int,view: View)
   }
    fun setOnItemClickListner(listener:onItemClickListner,view: View){   //,view: View
        fListener=listener
    }
  //  ,listener:onItemClickListner itemView:View
    class FavHolder(val binding: FavoriteViewBinding,listener:onItemClickListner):RecyclerView.ViewHolder(binding.root) {
//           val title=itemView.findViewById<TextView>(R.id.songs_name_fav)
//        val songImag=itemView.findViewById<ImageView>(R.id.imageMvFav)
//        val options=itemView.findViewById<ImageButton>(R.id.option_icon_fav)
        init {
            itemView.setOnClickListener {
             listener.onItemClick(absoluteAdapterPosition)
            }

          binding.optionIconFav.setOnClickListener{
                listener.onOptionClick(absoluteAdapterPosition,itemView)
            }
        }

    }

    private val diffCallBack=object :DiffUtil.ItemCallback<Music>(){
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
           return newItem==oldItem
        }



    }
    val differ=AsyncListDiffer(this,diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
       // val itemView=LayoutInflater.from(parent.context).inflate(R.layout.favorite_view,parent,false)
        return  FavHolder(FavoriteViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),fListener)//,fListener)
    }

    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val item= differ.currentList[position]
        holder.binding.songsNameFav.text=item.title

       Glide.with(holder.itemView.context).load(item.artUri)
           .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
           .into(holder.binding.imageMvFav)


    }

    override fun getItemCount()=differ.currentList.size

}