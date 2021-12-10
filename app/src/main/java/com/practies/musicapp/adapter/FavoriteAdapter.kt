package com.practies.musicapp.adapter

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practies.musicapp.Music
import com.practies.musicapp.R


class FavoriteAdapter(private val favoriteList:ArrayList<String>):RecyclerView.Adapter<FavoriteAdapter.FavHolder>() {
   // lateinit var fListener:onItemClickListner

  /* interface onItemClickListner{
       fun onItemClick(position: Int)
   }
    fun setOnItemClickListner(listener:onItemClickListner){
        fListener=listener
    }*/
  //  ,listener:onItemClickListner
    class FavHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
           val title=itemView.findViewById<TextView>(R.id.songs_name_fav)
       // val songImag=itemView.findViewById<ImageView>(R.id.imageMvFav)

        init {
            itemView.setOnClickListener {
             //  listener.onItemClick(adapterPosition)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.favorite_view,parent,false)
        return  FavHolder(itemView)//,fListener)
    }

    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val item=favoriteList[position]
        holder.title.text=item //.title
//       Glide.with(holder.itemView.context).load(favoriteList[position].artUri)
//           .apply(RequestOptions.placeholderOf(R.drawable.headphone).centerCrop())
//           .into(holder.songImag)
    }

    override fun getItemCount(): Int {
    return   favoriteList.size
    }
}