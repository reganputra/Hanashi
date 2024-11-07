package com.example.sutoriapuri.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sutoriapuri.data.response.ListStoryItem
import com.example.sutoriapuri.databinding.StoryRowBinding
import com.example.sutoriapuri.ui.detail.DetailActivity

class StoriesAdapter: ListAdapter<ListStoryItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = StoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val view: StoryRowBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(item: ListStoryItem) {
            Glide.with(view.root)
                .load(item.photoUrl)
                .into(view.ivItemPhoto)
            view.tvItemName.text = item.name
            view.tvItemDescription.text = item.description

            view.ivItemPhoto.setOnClickListener {
                val intent = Intent(view.root.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_STORY, item.id)
                view.root.context.startActivity(intent)
            }
        }

    }

    companion object {
        const val EXTRA_STORY = "extra_story"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}