package com.practies.musicapp.notifications

import android.content.*
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.practies.musicapp.MainActivity
import com.practies.musicapp.PlayScreenActivity
import com.practies.musicapp.model.musicServices

import com.practies.musicapp.service.MusicServices
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationIntent = Intent(context, MusicServices::class.java)
        notificationIntent.setAction( intent?.action)
        Log.i("MSG",intent?.action?: "null")

        context!!.startService(notificationIntent)

    }


}



//            ApplicationClass.EXit -> {
//                //Exit app   and notification
//                       musicServices!!.stopForeground(true)
//                   exitProcess(0)














