package com.practies.musicapp

import java.time.Duration
import java.util.concurrent.TimeUnit

object Constants {

    const val  CHANNEL_ID="channel1"
     const val MUSIC_NOTIFICATION_ID= 122


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