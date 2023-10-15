package com.syawal.storyapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntity>)

    @Query("DELETE FROM story")
    suspend fun deleteALl()

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getStory(): LiveData<List<StoryEntity>>

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getWidgetStory(): List<StoryEntity>
}