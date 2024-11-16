package com.example.sutoriapuri

import com.example.sutoriapuri.data.response.ListStoryItem

object DataDummy {
    fun generateDataDummy(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()

        for (i in 0..10) {
            val story = ListStoryItem(
                id = "$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "https://picsum.photos/200/300",
                createdAt = "2021-08-01"
            )
            storyList.add(story)
        }
        return storyList
    }
}