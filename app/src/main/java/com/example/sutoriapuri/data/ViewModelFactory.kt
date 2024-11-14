package com.example.sutoriapuri.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sutoriapuri.ui.MainViewModel
import com.example.sutoriapuri.ui.dashboard.UploadViewModel
import com.example.sutoriapuri.ui.detail.DetailViewModel
import com.example.sutoriapuri.ui.home.HomeViewModel
import com.example.sutoriapuri.ui.login.LoginViewModel
import com.example.sutoriapuri.ui.map.MapViewModel

import com.example.sutoriapuri.ui.register.RegisterViewModel

class ViewModelFactory(private val storyRepository: StoryRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(storyRepository) as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}