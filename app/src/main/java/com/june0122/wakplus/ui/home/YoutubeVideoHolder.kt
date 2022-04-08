package com.june0122.wakplus.ui.home

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entitiy.YoutubeUserInfo
import com.june0122.wakplus.data.entitiy.YoutubeVideoInfo
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding
import com.june0122.wakplus.utils.Language
import com.june0122.wakplus.utils.timeAgo
import com.june0122.wakplus.utils.withSuffix

class YoutubeVideoHolder(binding: ItemYoutubeVideoBinding): RecyclerView.ViewHolder(binding.root) {
    private val thumbnailImageView = binding.imgThumbnail
    private val channelProfileImageView = binding.imgChannelProfile
    private val videoTitleTextView = binding.tvVideoTitle
    private val channelNameTextView = binding.tvChannelName
    private val viewCountTextView = binding.tvViewCount
    private val elapesdTimeTextView = binding.tvElapsedTime

    fun bind(userInfo: YoutubeUserInfo, videoInfo: YoutubeVideoInfo) {
        thumbnailImageView.load(videoInfo.snippet.thumbnails.high.url) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }

        channelProfileImageView.load(userInfo.snippet.thumbnails.high.url) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }

        videoTitleTextView.text = videoInfo.snippet.title
        channelNameTextView.text = videoInfo.snippet.channelTitle
        viewCountTextView.text =
            itemView.context.getString(R.string.view_count, videoInfo.statistics.viewCount.withSuffix(Language.KR))

        elapesdTimeTextView.text = videoInfo.snippet.publishedAt.timeAgo()
    }
}