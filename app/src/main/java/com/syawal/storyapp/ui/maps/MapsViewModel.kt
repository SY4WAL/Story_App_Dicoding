package com.syawal.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.StoryRepository

class MapsViewModel(private val repository: StoryRepository): ViewModel() {
    fun getStoryWithLocation() = repository.getStoryWithLocation()
}