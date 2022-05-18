package com.june0122.wakplus.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.IdSet
import com.june0122.wakplus.data.repository.TwitchRepository
import com.june0122.wakplus.data.repository.YoutubeRepository
import com.june0122.wakplus.utils.SNS
import retrofit2.HttpException
import java.io.IOException

class ContentPagingSource(
    private val idSet: IdSet,
    private val contentType: Int,
    private val profileUrl: String,
    private val twitchRepository: TwitchRepository,
    private val youtubeRepository: YoutubeRepository,
) : PagingSource<Int, Content>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> {
        val position = params.key ?: 0
        return try {
            val response = when (contentType) {
                SNS.TWITCH -> {
                    twitchRepository.getTwitchVideos(idSet)
                }
                SNS.YOUTUBE -> {
                    youtubeRepository.getYoutubeVideos(idSet, profileUrl)
                }
                else -> null
            }

            val data = response ?: listOf()
            val nextKey = if (data.isEmpty()) {
                null
            } else {
                null
//                position + NETWORK_PAGE_SIZE
            }
            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Content>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(NETWORK_PAGE_SIZE) ?: anchorPage?.nextKey?.minus(NETWORK_PAGE_SIZE)

//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(20)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(20)
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}