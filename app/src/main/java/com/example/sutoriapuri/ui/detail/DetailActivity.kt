package com.example.sutoriapuri.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sutoriapuri.data.Result
import com.example.sutoriapuri.data.ViewModelFactory
import com.example.sutoriapuri.databinding.ActivityDetailBinding
import com.example.sutoriapuri.ui.adapter.StoriesAdapter.Companion.EXTRA_STORY

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDetailStories()
    }

    private fun getDetailStories(){
        val storyId = intent.getStringExtra(EXTRA_STORY)
        if (storyId != null){
            detailViewModel.getDetailStory(storyId).observe(this){
                when(it){
                    is Result.Loading -> showLoading(true)

                    is Result.Success -> {
                        showLoading(false)
                        val story = it.data
                        binding.tvDetailName.text = story.name
                        binding.tvDetailDescription.text = story.description
                        Glide.with(this)
                            .load(story.photoUrl)
                            .into(binding.ivDetailPhoto)
                    }

                    is Result.Error ->{
                        showLoading(false)
                        Toast.makeText(this, "Failed to load story details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}