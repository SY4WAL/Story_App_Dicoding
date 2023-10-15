package com.syawal.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syawal.storyapp.data.AuthRepository
import com.syawal.storyapp.di.Injection
import com.syawal.storyapp.ui.login.LoginViewModel
import com.syawal.storyapp.ui.register.RegisterViewModel
import com.syawal.storyapp.ui.welcome.WelcomeViewModel

class AuthViewModelFactory(private val repository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): AuthViewModelFactory {
            return INSTANCE ?: synchronized(AuthViewModelFactory::class.java) {
                INSTANCE ?: AuthViewModelFactory(
                    Injection.provideAuthRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}