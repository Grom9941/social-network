package com.example.socialnetwork.model

import androidx.room.TypeConverter
import com.example.socialnetwork.model.dataclass.Friend
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromListFriendToJson(value: List<Friend>?): String? = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToListFriend(value: String?): List<Friend>? {
        val turnsType = object : TypeToken<List<Friend>>() {}.type
        return Gson().fromJson<List<Friend>>(value, turnsType)
    }
}