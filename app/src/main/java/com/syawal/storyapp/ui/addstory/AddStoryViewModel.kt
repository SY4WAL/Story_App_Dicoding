package com.syawal.storyapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.Repository
import java.io.File

class AddStoryViewModel(private val repository: Repository) : ViewModel() {

    fun uploadStory(imgFile: File, desc: String) = repository.uploadStory(imgFile, desc)

}