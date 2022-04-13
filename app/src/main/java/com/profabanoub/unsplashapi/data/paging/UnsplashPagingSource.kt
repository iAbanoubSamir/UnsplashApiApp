package com.profabanoub.unsplashapi.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.profabanoub.unsplashapi.data.remote.api.UnsplashAPI
import com.profabanoub.unsplashapi.data.remote.model.PhotoModel
import retrofit2.HttpException
import java.io.IOException

const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashAPI: UnsplashAPI,
    private val query: String
) : PagingSource<Int, PhotoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashAPI.searchPhotos(query, position, params.loadSize)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoModel>): Int? {
        return UNSPLASH_STARTING_PAGE_INDEX
    }
}