package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entitiy.YoutubeVideoInfo
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding
import com.june0122.wakplus.utils.Language
import com.june0122.wakplus.utils.timeAgo
import com.june0122.wakplus.utils.withSuffix

class YoutubeVideoHolder(private val binding: ItemYoutubeVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(profileUrl: String, videoInfo: YoutubeVideoInfo) = with(binding) {
        imgThumbnail.load(videoInfo.snippet.thumbnails.high.url) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }

        imgChannelProfile.load(profileUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }

        tvDuration.text = videoInfo.contentDetails.duration
        tvVideoTitle.text = videoInfo.snippet.title
        tvChannelName.text = videoInfo.snippet.channelTitle
        tvViewCount.text =
            itemView.context.getString(R.string.view_count, videoInfo.statistics.viewCount.withSuffix(Language.KR))
        tvElapsedTime.text = videoInfo.snippet.publishedAt.timeAgo(binding.root.context)
    }
}