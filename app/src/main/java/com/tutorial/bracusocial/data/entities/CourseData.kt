package com.tutorial.bracusocial.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CourseData(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val courseName: String,
    val section: String,
    val classDay: String? = null,
    val classTime: String? = null,
    val classRoom: String? = null,
    val labDay: String? = null,
    val labTime: String? = null,
    val labRoom: String? = null
)
