package com.practies.musicapp.notifications

import android.content.*
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.practies.musicapp.MainActivity
import com.practies.musicapp.PlayScreenActivity

import com.practies.musicapp.service.MusicServices
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {

    val services:MusicServices?=null

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationIntent = Intent(context, MusicServices::class.java)
       // notificationIntent.putExtra("Action", intent?.action)

      // peekService(context, notificationIntent)

        when (intent?.action) {
            ApplicationClass.PLAY -> {
                Toast.makeText(context, "preve button", Toast.LENGTH_SHORT).show()

//        services!!.playSong()
    }


}}}


//        when (intent?.action) {
//            ApplicationClass.PREVIOUS -> {  //previous song
//                Toast.makeText(context, "preve button", Toast.LENGTH_SHORT).show()
//
//            }
//            ApplicationClass.PLAY -> {           //play or pause song
//                Toast.makeText(context, "play button", Toast.LENGTH_SHORT).show()
//
//
//            }
////              if (musicServices!!.mediaPlayer.isPlaying){
////                  musicServices!!.playPauseMusic(false)
////                }else
////                    musicServices!!.playPauseMusic(true)
////          }
//            ApplicationClass.NEXT -> {          //next song
//                Toast.makeText(context, "Next button", Toast.LENGTH_SHORT).show()
//                  musicServices!!.nextPreviousSong(true)
//            }
//            ApplicationClass.EXit -> {
//                //Exit app   and notification
//                       musicServices!!.stopForeground(true)
//                   exitProcess(0)














