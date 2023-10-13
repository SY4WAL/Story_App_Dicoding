package com.syawal.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.StoryRepository
import com.syawal.storyapp.di.Injection
import com.syawal.storyapp.ui.welcome.WelcomeViewModel

class TestFactory(private val repository: StoryRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(com.syawal.storyapp.ui.home.ViewModel::class.java) -> {
                com.syawal.storyapp.ui.home.ViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TestFactory? = null
        @JvmStatic
        fun getInstance(context: Context): TestFactory {
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java) {
//                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
//                }
//            }
//            return INSTANCE as ViewModelFactory
            return INSTANCE ?: synchronized(TestFactory::class.java) {
                INSTANCE ?: TestFactory(
                    Injection.provideStoryRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}