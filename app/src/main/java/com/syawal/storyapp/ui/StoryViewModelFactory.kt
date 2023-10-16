package com.syawal.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syawal.storyapp.data.StoryRepository
import com.syawal.storyapp.di.Injection
import com.syawal.storyapp.ui.addstory.AddStoryViewModel
import com.syawal.storyapp.ui.detail.DetailViewModel
import com.syawal.storyapp.ui.home.HomeViewModel
import com.syawal.storyapp.ui.maps.MapsViewModel

class StoryViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): StoryViewModelFactory {
            return INSTANCE ?: synchronized(StoryViewModelFactory::class.java) {
                INSTANCE ?: StoryViewModelFactory(
                    Injection.provideStoryRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}