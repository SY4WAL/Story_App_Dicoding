package com.syawal.storyapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.StoryRepository
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun uploadStory(imgFile: File, desc: String) = repository.uploadStory(imgFile, desc)
}