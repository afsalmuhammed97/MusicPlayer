package com.practies.musicapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.practies.musicapp.R

class CustomDialogFragment:DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView:View=inflater.inflate(R.layout.play_list_menu,container,false)
        return  rootView
    }
}