package com.tutorial.bracusocial.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Misc(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val login: Boolean,
    val loginID: Int,
    val dbState: Boolean,
    val dbUpdateDate: String
)
