package com.syawal.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syawal.storyapp.data.AuthRepository
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.api.response.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(loginResult: LoginResult) {
        viewModelScope.launch {
            repository.saveSession(loginResult)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}