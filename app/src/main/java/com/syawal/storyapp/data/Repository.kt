package com.syawal.storyapp.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.syawal.storyapp.data.api.ApiService
import com.syawal.storyapp.data.api.response.DetailStoryResponse
import com.syawal.storyapp.data.api.response.LoginResponse
import com.syawal.storyapp.data.api.response.LoginResult
import com.syawal.storyapp.data.api.response.RegisterResponse
import com.syawal.storyapp.data.api.response.StoryResponse
import com.syawal.storyapp.data.pref.UserModel
import com.syawal.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveToken(user: UserModel) {
        userPreference.saveToken(user)
    }

    fun getToken(): Flow<UserModel> {
        return userPreference.getToken()
    }

    suspend fun saveSession(loginResult: LoginResult) {
        userPreference.saveSession(loginResult)
    }

    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStories().listStory
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

//    fun getStories(token: String) = liveData {
//        emit(ResultState.Loading)
//        try {
//            val successResponse = apiService.getStories(token).listStory
//            emit(ResultState.Success(successResponse))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
//            emit(ResultState.Error(errorResponse.message))
//        }
//    }

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
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}