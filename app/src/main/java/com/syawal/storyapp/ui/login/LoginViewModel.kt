package com.syawal.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.api.response.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(loginResult: LoginResult) {
        viewModelScope.launch {
            repository.saveSession(loginResult)
        }
    }
}