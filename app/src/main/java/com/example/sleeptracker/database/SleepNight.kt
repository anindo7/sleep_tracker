package com.example.sleeptracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_sleep_data_table")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)
    var nightId: Long = 0L,
    @ColumnInfo(name="start_time")
    val startTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name="end_time")
    var endTime: Long = startTime,
    @ColumnInfo(name="rating")
    var rating: Int = -1
)