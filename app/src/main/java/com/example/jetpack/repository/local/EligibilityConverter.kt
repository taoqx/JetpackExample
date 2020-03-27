package com.example.jetpack.repository.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EligibilityConverter {
    @TypeConverter
    fun revert(content: String): List<String> {
        return Gson().fromJson(
            content,
            object : TypeToken<List<String>>() {}.type
        )
    }

    @TypeConverter
    fun converter(list: List<String>): String {
        return Gson().toJson(list)
    }
}