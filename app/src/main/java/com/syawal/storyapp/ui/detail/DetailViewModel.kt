package com.syawal.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.StoryRepository

class DetailViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getDetailStory(id: String) = repository.getDetailStory(id)
}