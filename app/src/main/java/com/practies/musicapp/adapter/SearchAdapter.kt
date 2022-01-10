package com.practies.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practies.musicapp.databinding.SerchItemViewBinding
import com.practies.musicapp.model.model2.Music

class SearchAdapter (val list:ArrayList<Music>):RecyclerView.Adapter<SearchAdapter.MyHolder>() {
    class MyHolder(val binding: SerchItemViewBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
      return MyHolder(SerchItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val item=list[position]
        holder.binding.songsNameMv.text=item.title
    }

    override fun getItemCount()= list.size

}