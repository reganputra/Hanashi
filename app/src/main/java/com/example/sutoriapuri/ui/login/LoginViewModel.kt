package com.example.sutoriapuri.ui.login

import androidx.lifecycle.ViewModel
import com.example.sutoriapuri.data.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun userLogin(email: String, password: String) = storyRepository.userLogin(email,password)
}