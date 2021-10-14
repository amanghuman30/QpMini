package com.qpmini.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "chats"
)
data class Chats(
    var participantId : UUID,
    var creatorId : UUID
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}