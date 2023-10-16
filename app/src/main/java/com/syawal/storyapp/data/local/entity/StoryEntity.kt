package com.syawal.storyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var photoUrl: String,
    var name: String,
    var description: String,
    var createdAt: String,
    var lon: Double? = null,
    val lat: Double? = null
)