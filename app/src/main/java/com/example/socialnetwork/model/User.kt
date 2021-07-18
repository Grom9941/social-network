package com.example.socialnetwork.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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
    @ColumnInfo(name = "about")
    @SerializedName("about")
    val aboutUser: String,
    val registered: String,
    val latitude: Float,
    val longitude: Float,
    // TODO: Enum class
    val favoriteFruit: String,
    val friends: ArrayList<Int>
)
