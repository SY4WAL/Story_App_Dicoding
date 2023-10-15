package com.syawal.storyapp.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.syawal.storyapp.data.AuthRepository
import com.syawal.storyapp.data.api.response.LoginResult

class WelcomeViewModel (private val repository: AuthRepository) : ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}