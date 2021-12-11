package com.practies.musicapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practies.musicapp.model.FavoriteMusic

@Database(entities = [FavoriteMusic::class], version = 1, exportSchema = false)
abstract class FavoriteDataBase :RoomDatabase() {

    abstract fun musicDao():MusicDao

companion object{
    @Volatile
private var INSTANCE :FavoriteDataBase ?=null

    fun getDatabase(context: Context):FavoriteDataBase{
        val tempIntsance= INSTANCE
        if (tempIntsance !=null){
            return tempIntsance
        }
        synchronized(this){
            val instance= Room.databaseBuilder(
                context.applicationContext,
                FavoriteDataBase::class.java,
                "favorites"
            ).build()
             INSTANCE=instance
            return  instance
        }
    }

}


}