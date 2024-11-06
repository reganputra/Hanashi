package com.example.sutoriapuri.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getAllStories()
}