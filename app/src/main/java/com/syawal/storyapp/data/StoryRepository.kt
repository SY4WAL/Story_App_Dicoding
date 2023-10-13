package com.syawal.storyapp.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.syawal.storyapp.data.api.ApiService
import com.syawal.storyapp.data.api.response.StoryResponse
import com.syawal.storyapp.data.pref.UserPreference
import retrofit2.HttpException

class StoryRepository private constructor(
    private val apiService: ApiService,
) {
    fun getStories(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesToken(token).listStory
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}