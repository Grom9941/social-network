package com.example.socialnetwork.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromListToJson(value: ArrayList<Int>?): String? = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToList(value: String?): ArrayList<Int>? {
        val turnsType = object: TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson<ArrayList<Int>>(value, turnsType)
    }
}