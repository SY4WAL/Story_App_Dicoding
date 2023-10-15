package com.syawal.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.AuthRepository

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}