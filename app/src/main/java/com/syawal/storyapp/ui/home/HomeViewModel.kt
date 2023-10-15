package com.syawal.storyapp.ui.home

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.StoryRepository

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStories() = repository.getStories()
}