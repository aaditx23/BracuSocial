package com.tutorial.bracusocial.misc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Misc::class],
    version = 1
)

abstract class MiscDatabase: RoomDatabase(){

    abstract val dao:MiscDao

    companion object {
        private var instance: MiscDatabase? = null

        // Create or retrieve an instance of the database
        fun getInstance(context: Context): MiscDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    MiscDatabase::class.java,
                    "misc_data.db"
                ).build()
                instance = database
                database
            }
        }
    }

}