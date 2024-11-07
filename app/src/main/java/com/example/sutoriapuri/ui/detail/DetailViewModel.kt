package com.example.sutoriapuri.ui.detail

import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository

class DetailViewModel(private val storyRepository: StoryRepository): ViewModel(){
    fun getDetailStory(id: String) = storyRepository.getStoryById(id)
}