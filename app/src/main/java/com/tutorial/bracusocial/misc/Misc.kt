package com.tutorial.bracusocial.misc

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Misc(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val login: Boolean,
    val loginID: Int,
    val db_state: Boolean,
    val db_update_date: String
)
