package com.practies.musicapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.practies.musicapp.service.MusicServices

class NotificationReceiver:BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationIntent = Intent(context, MusicServices::class.java)
        notificationIntent.setAction( intent?.action)
        Log.i("MSG",intent?.action?: "null")

        context!!.startService(notificationIntent)

    }


}
















