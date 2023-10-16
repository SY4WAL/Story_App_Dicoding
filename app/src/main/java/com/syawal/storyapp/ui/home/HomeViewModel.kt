package com.syawal.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.syawal.storyapp.data.StoryRepository
import com.syawal.storyapp.data.api.response.ListStoryItem
import com.syawal.storyapp.data.local.entity.StoryEntity

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<StoryEntity>> =
        repository.getStory().cachedIn(viewModelScope)
}