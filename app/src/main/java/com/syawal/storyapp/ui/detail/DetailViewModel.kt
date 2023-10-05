package com.syawal.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.Repository

class DetailViewModel(private val repository: Repository) : ViewModel() {

    fun getDetailStory(id: String) = repository.getDetailStory(id)
}