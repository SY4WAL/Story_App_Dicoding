package com.syawal.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.syawal.storyapp.data.api.ApiService
import com.syawal.storyapp.data.api.response.DetailStoryResponse
import com.syawal.storyapp.data.api.response.StoryResponse
import com.syawal.storyapp.data.local.StoryDao
import com.syawal.storyapp.data.local.StoryEntity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: StoryDao
) {
    fun getStories() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStories().listStory
            val storyList = successResponse.map { story ->
                StoryEntity(
                    story.id,
                    story.photoUrl,
                    story.name,
                    story.description,
                    story.createdAt,
                    story.lon,
                    story.lat
                )
            }
            storyDao.deleteALl()
            storyDao.insertStory(storyList)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
        val localData: LiveData<ResultState<List<StoryEntity>>> =
            storyDao.getStory().map { ResultState.Success(it) }
        emitSource(localData)
    }

    fun getStoryWithLocation() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesWithLocation()
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.toString()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun getDetailStory(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetailStory(id).story
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun uploadStory(imgFile: File, desc: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imgFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imgFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao
        ): StoryRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(apiService, storyDao)
            }.also { INSTANCE = it }
    }
}