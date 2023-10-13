package com.syawal.storyapp.ui.home

import com.syawal.storyapp.data.StoryRepository


class ViewModel (private val repository: StoryRepository) {

    fun getStoriesToken(token: String) = repository.getStories(token)

}