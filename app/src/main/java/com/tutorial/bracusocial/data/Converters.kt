package com.tutorial.bracusocial.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun mapToString(map:  MutableMap<String, MutableMap<String, MutableList<Int>>>): String{
        return Gson().toJson(map)

    }
    @TypeConverter
    fun stringToMap(string: String):  MutableMap<String, MutableMap<String, MutableList<Int>>>{
        val mapType = object: TypeToken<MutableMap<String, MutableMap<String, MutableList<Int>>>>() {}.type
        return Gson().fromJson(string, mapType)
    }

    @TypeConverter
    fun listToString(list: MutableList<Int>): String{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun stringToList(string: String): MutableList<Int>{
        val listType = object: TypeToken<MutableList<Int>>() {}.type
        return Gson().fromJson(string, listType)
    }
}