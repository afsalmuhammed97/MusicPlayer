package com.practies.musicapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practies.musicapp.fragments.AlSongsFragment
import com.practies.musicapp.fragments.PlaylistFragment
import com.practies.musicapp.fragments.favoriteFragment

class ViewPageAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
           return     when(position){

                    0 -> {  AlSongsFragment()

                    }
                    1-> {favoriteFragment()

                    }
                    2->{ PlaylistFragment()


                    }


                    else->{
                        Fragment()
                    }
                }
    }
}