package com.example.sutoriapuri.ui.home


import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getAllStories()
}