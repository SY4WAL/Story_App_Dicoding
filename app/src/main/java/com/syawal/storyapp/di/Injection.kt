package com.syawal.storyapp.di

import android.content.Context
import com.syawal.storyapp.data.AuthRepository
import com.syawal.storyapp.data.StoryRepository
import com.syawal.storyapp.data.api.ApiConfig
import com.syawal.storyapp.data.local.StoryDatabase
import com.syawal.storyapp.data.pref.UserPreference
import com.syawal.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return AuthRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, dao)
    }
}