package com.practies.musicapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practies.musicapp.fragments.AlSongsFragment
import com.practies.musicapp.fragments.FavouriteFragment

import com.practies.musicapp.fragments.PlaylistFragment

class ViewPageAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
           return     when(position){

                    0 -> {
                        AlSongsFragment()
                    }
                    1-> {
                        FavouriteFragment()
                    }
                    2->{
                       PlaylistFragment()
                    }
                    else->{
                       // PlaylistFragment()
                        Fragment()
                    }
                }
    }
}