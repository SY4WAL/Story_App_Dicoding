package com.syawal.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.api.response.LoginResult
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() = repository.getStories()

}