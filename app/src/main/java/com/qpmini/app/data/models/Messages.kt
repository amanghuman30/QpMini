package com.qpmini.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "messages"
)
data class Messages(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var chatId : Int,
    var senderId : UUID,
    var messageType : String,
    var messageBody : String,
    var timestamp : Date
)
