package com.example.socialnetwork.model.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val id: Int,
    val isActive: Boolean,
    val age: Byte,
    // TODO: Enum class
    val eyeColor: String,
    val name: String,
    val company: String,
    val email: String,
    val phone: String,
    val address: String,
    val about: String,
    val registered: String,
    val latitude: Float,
    val longitude: Float,
    val friends: List<Friend>,
    // TODO: Enum class
    val favoriteFruit: String
)
