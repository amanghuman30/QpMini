package com.qpmini.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qpmini.app.util.MessageType
import java.util.*

@Entity(
    tableName = "messages"
)
data class Messages(
    var chatId : Int,
    var senderId : UUID,
    var messageType : MessageType,
    var messageBody : String,
    var timestamp : Date
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

