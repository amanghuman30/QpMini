package com.qpmini.app.data.source.local

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun timestampFromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun dateFromTimestamp(timestamp: Long): Date {
        return Date(timestamp)
    }
}