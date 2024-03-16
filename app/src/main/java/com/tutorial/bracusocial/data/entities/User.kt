package com.tutorial.bracusocial.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val studentID: Int,
    val name: String,
    val password: String,
    var courses:  MutableMap<String, MutableMap<String, MutableList<Int>>>,
    var friends: MutableList<Int>

)

class CourseMapConverter{
    @TypeConverter
    fun mapToString(map:  MutableMap<String, MutableMap<String, MutableList<Int>>>): String{
        return Gson().toJson(map)

    }
    @TypeConverter
    fun stringToMap(string: String):  MutableMap<String, MutableMap<String, MutableList<Int>>>{
        val mapType = object: TypeToken< MutableMap<String, MutableMap<String, MutableList<Int>>>>() {}.type
        return Gson().fromJson(string, mapType)
    }
}

class FriendListConverter{
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

