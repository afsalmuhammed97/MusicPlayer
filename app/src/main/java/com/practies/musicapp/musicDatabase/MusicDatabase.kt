package com.practies.musicapp.musicDatabase

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practies.musicapp.model.Music


@Database(entities = [Music::class], version = 2, exportSchema = true)
 abstract class MusicDatabase :RoomDatabase() {

     abstract fun musicDao():MusicDao

     companion object{

         @Volatile
         private var INSTANCE :MusicDatabase?=null

//         val migration_1_2:Migration=object :Migration(1,2){
//             override fun migrate(database: SupportSQLiteDatabase) {
//                 database.execSQL("ALTER TABLE allMusics  ADD  COLUMN  timeStamp  DEFAULT '' ")
//                 database.execSQL("ALTER TABLE allMusics  ADD   COLUMN  play_list_name  DEFAULT '' ")
//             }
//
//         }

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
                 )      //.addMigrations(migration_1_2)
                     .build()
                 INSTANCE=instance
                 return  instance
             }
         }






     }




     }














