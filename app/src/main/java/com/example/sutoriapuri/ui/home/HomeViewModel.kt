package com.example.sutoriapuri.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sutoriapuri.data.StoryRepository
import com.example.sutoriapuri.data.response.ListStoryItem

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>>{
        return storyRepository.storiesPaging().cachedIn(viewModelScope)
    }
}