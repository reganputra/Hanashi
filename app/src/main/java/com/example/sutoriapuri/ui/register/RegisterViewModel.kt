package com.example.sutoriapuri.ui.register

import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        storyRepository.userRegister(name, email, password)

}