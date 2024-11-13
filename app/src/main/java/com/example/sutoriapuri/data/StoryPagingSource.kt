package com.example.sutoriapuri.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sutoriapuri.data.api.ApiService
import com.example.sutoriapuri.data.response.ListStoryItem
import com.example.sutoriapuri.data.userpref.UserPreference
import kotlinx.coroutines.flow.first

class StoryPagingSource(
    private val apiService: ApiService,
    private val userPreference: UserPreference
): PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
       return try {
           val page = params.key?: INITIAL_PAGE_INDEX
           val token = userPreference.getSession().first().tokenKey
           val authToken = "Bearer $token"
           val response = apiService.getStories(authToken, page, params.loadSize)
           val stories = response.listStory

           LoadResult.Page(
               data = stories,
               prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
               nextKey = if (stories.isEmpty()) null else page + 1
           )
       } catch (exception: Exception){
           LoadResult.Error(exception)
       }
    }
}