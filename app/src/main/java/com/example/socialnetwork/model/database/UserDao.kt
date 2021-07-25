package com.example.socialnetwork.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.socialnetwork.model.dataclass.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    //TODO: LiveData to Flow
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE id=:id")
    fun getUserInfoById(id: Int): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(user: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(user: User)
}