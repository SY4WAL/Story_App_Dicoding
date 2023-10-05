package com.syawal.storyapp.di

import android.content.Context
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.api.ApiConfig
import com.syawal.storyapp.data.pref.UserPreference
import com.syawal.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(pref, apiService)
    }
}