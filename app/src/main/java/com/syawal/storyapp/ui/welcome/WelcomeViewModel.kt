package com.syawal.storyapp.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.api.response.LoginResult

class WelcomeViewModel (private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}