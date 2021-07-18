package com.example.socialnetwork.model.database

import androidx.room.*
import com.example.socialnetwork.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT favoriteFruit FROM user_table WHERE id=:id")
    fun getFriendsById(id: Int): Flow<String>

    @Query("SELECT * FROM user_table WHERE id=:id")
    fun getUserInfoById(id: Int): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}