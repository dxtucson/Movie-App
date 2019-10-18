package com.d.x.movie.app.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {
    @TypeConverter
    fun strToIntList(str: String?): List<Int> {
        if (str == null) {
            return listOf()
        }
        val listType = object : TypeToken<List<Int>>() {
        }.type
        return Gson().fromJson<List<Int>>(str, listType)
    }

    @TypeConverter
    fun intListToStr(list: List<Int>): String {
        return Gson().toJson(list)
    }
}