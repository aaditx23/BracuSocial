package com.tutorial.bracusocial.misc

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MiscDao {

    @Upsert
    suspend fun upsertMisc(course: Misc)

    @Delete
    suspend fun deleteMisc(course: Misc)

    @Query("select * from Misc where login = :flag")
    fun getMiscInfo(flag: Boolean): Misc?
}