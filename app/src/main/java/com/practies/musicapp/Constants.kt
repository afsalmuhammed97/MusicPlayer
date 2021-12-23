package com.practies.musicapp

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.concurrent.TimeUnit


object Constants {

    const val  CHANNEL_ID="channel1"
     const val MUSIC_NOTIFICATION_ID= 122
           const val PLAY=1
           const val PAUSE=2

           const val favorite="favorite"


}


object Constant {
    fun getDefaultAlbumArt(context: Context): Bitmap? {
        var bm: Bitmap? = null
        val options = BitmapFactory.Options()
        try {
            bm = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.sym_def_app_icon  , options
            )
        } catch (ee: Error) {
        } catch (e: Exception) {
        }
        return bm
    }

    interface ACTION {
        companion object {
            const val MAIN_ACTION = "com.marathons.customnotification.action.main"
            const val INIT_ACTION = "com.marathons.customization.action.init"
            const val PREV_ACTION = "com.marothiatechs.customnotification.action.prev"
            val PLAY_ACTION: String = "com.marothiatechs.customnotification.action.play"
            const val NEXT_ACTION = "com.marothiatechs.customnotification.action.next"
            const val STARTFOREGROUND_ACTION =
                "com.marothiatechs.customnotification.action.startforeground"
            const val STOPFOREGROUND_ACTION =
                "com.marothiatechs.customnotification.action.stopforeground"
        }

    }

    interface NOTIFICATION_ID {
        companion object {
            const val FOREGROUND_SERVICE = 101
        }
    }
}






fun durationConverter(duration: Long):String{
       return String.format(
           "%02d:%02d",
           TimeUnit.MICROSECONDS.toMillis(duration),
           TimeUnit.MICROSECONDS.toSeconds(duration)-
TimeUnit.MINUTES.toSeconds(
    TimeUnit.MILLISECONDS.toMinutes(duration)
))





}