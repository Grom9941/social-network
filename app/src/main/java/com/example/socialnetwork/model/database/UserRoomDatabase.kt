package com.example.socialnetwork.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.socialnetwork.model.Converters
import com.example.socialnetwork.model.dataclass.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DB_NAME = "user_database"

        @Volatile
        private var ROOM_INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase {
            return ROOM_INSTANCE ?: synchronized(this) {
                val instanceTemporary = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    DB_NAME
                ).build()
                ROOM_INSTANCE = instanceTemporary
                instanceTemporary
            }
        }
    }
}