package com.example.socialnetwork.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.socialnetwork.model.dataclass.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUsers(): Flow<List<User>>

    /*
    @Query("SELECT friends FROM user_table WHERE id=:id")
    fun getFriendsById(id: Int): Flow<List<Friend>>
     */

    @Query("SELECT * FROM user_table WHERE id=:id")
    fun getUserInfoById(id: Int): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}