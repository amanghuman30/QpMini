package com.qpmini.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: UUID,
    val avatarURL: String? = null,
    val name: String? = null
)