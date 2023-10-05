package com.syawal.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.syawal.storyapp.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}