package com.practies.musicapp.musicDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practies.musicapp.Music


@Database(entities = [Music::class], version = 1, exportSchema = false)
 abstract class MusicDatabase :RoomDatabase() {

     abstract fun musicDao():MusicDao

     companion object{

         @Volatile
         private var INSTANCE :MusicDatabase?=null

         fun getDatabase(context: Context):MusicDatabase {
             val tempIntsance= INSTANCE
             if (tempIntsance !=null){
                 return tempIntsance
             }
             synchronized(this){
                 val instance= Room.databaseBuilder(
                     context.applicationContext,
                    MusicDatabase::class.java,
                     "favorites"
                 ).build()
                 INSTANCE=instance
                 return  instance
             }
         }

     }




     }














