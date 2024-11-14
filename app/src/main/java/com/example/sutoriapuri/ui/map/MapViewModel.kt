package com.example.sutoriapuri.ui.map

import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getLocation() = storyRepository.getStoriesWithLocation()
}