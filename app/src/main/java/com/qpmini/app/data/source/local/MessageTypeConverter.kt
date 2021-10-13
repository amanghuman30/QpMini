package com.qpmini.app.data.source.local

import androidx.room.TypeConverter
import com.qpmini.app.util.MessageType
import java.util.*

class MessageTypeConverter {
    @TypeConverter
    fun fromType(type: MessageType): String {
        return type.name
    }

    @TypeConverter
    fun toType(string: String): MessageType {
        return MessageType.valueOf(string)
    }
}