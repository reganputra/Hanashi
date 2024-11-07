package com.example.sutoriapuri.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DashboardViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody) = storyRepository.uploadStory(file, description)
}