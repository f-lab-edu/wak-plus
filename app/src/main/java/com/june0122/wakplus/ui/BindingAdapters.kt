package com.june0122.wakplus.ui

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.google.android.material.chip.Chip
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.network.NetworkState
import com.june0122.wakplus.ui.home.viewholder.TwitchVideoHolder.Companion.RES_STANDARD
import com.june0122.wakplus.utils.Language
import com.june0122.wakplus.utils.NETWORK.RECONNECTION_UI_VISIBLE_TIME
import com.june0122.wakplus.utils.SNS
import com.june0122.wakplus.utils.timeAgo
import com.june0122.wakplus.utils.withSuffix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("circularImageUrl")
    fun setCircularImage(view: ImageView, imageUrl: String) {
        view.load(imageUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }
    }

    @JvmStatic
    @BindingAdapter("thumbnailUrl")
    fun setThumbnail(view: ImageView, imageUrl: String) {
        if (imageUrl != "") {
            view.load(imageUrl) {
                scale(Scale.FIT)
                crossfade(true)
                crossfade(300)
            }
        } else {
            view.load(R.drawable.placeholder_twitch_live) {
                scale(Scale.FIT)
                crossfade(true)
                crossfade(300)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("twitchThumbnailUrl")
    fun setTwitchThumbnailUrl(view: ImageView, imageUrl: String) {
        setThumbnail(view, imageUrl.parseTwitchThumbnailUrl())
    }

    @JvmStatic
    @BindingAdapter("viewCount")
    fun setViewCount(view: TextView, viewCount: String) {
        view.text = view.context.getString(R.string.view_count, viewCount.withSuffix(Language.KR))
    }

    @JvmStatic
    @BindingAdapter("elapsedTime")
    fun setElapsedTime(view: TextView, elapsedTime: String) {
        view.text = elapsedTime.timeAgo(view.context)
    }

    private fun String.parseTwitchThumbnailUrl(): String = this.replace("%{width}x%{height}", RES_STANDARD)

    /** Duration Parsing */
    private val regexMap = HashMap<String, String>()
    private const val oneDigit = "(?<=^|\\D)(\\d)(?=\\D)" // ?????? ????????? ??? ???????????? ????????? ?????? ?????? ?????????
    private const val twoDigits = "0$1"

    @JvmStatic
    @BindingAdapter("duration")
    fun parseDuration(view: TextView, duration: String) {
        storeRegexes()
        val date = duration.replace(oneDigit.toRegex(), twoDigits).run { // ?????? ????????? ??? ???????????? ??? ??? ???????????? ??????
            if (this.first() == 'P') {  // Youtube??? Twitch??? duration ?????? ?????????
                this.substring(2)
            } else {
                this.uppercase()
            }
        }
        val literal = date.regexLiteral()
        val parsedDate = date.replace(literal.toRegex(), regexMap[literal] ?: "$1:$2:$3")

        if (parsedDate.first() == '0') {
            view.text = parsedDate.substring(1)
        } else {
            view.text = parsedDate
        }
    }

    private fun String.regexLiteral(): String {
        for (regex in regexMap.keys) {
            if (Pattern.matches(regex, this)) return regex
        }

        return "(\\d\\d)H(\\d\\d)M(\\d\\d)S"
    }

    private fun storeRegexes() {
        regexMap["(\\d\\d)S"] = "00:$1"
        regexMap["(\\d\\d)M"] = "$1:00"
        regexMap["(\\d\\d)H"] = "$1:00:00"
        regexMap["(\\d\\d)M(\\d\\d)S"] = "$1:$2"
        regexMap["(\\d\\d)H(\\d\\d)S"] = "$1:00:$2"
        regexMap["(\\d\\d)H(\\d\\d)M"] = "$1:$2:00"
        regexMap["(\\d\\d)H(\\d\\d)M(\\d\\d)S"] = "$1:$2:$3"
    }

    @JvmStatic
    @BindingAdapter("snsName")
    fun setSnsName(view: Chip, sns: SnsPlatformEntity) {
        val stringRes = when (sns.serviceId) {
            SNS.ALL -> R.string.sns_all
            SNS.YOUTUBE -> R.string.sns_youtube
            SNS.TWITCH -> R.string.sns_twitch
            SNS.INSTAGRAM -> R.string.sns_instagram
            SNS.TWITTER -> R.string.sns_twitter
            SNS.NAVER_CAFE -> R.string.sns_naver_cafe
            SNS.SOUNDCLOUD -> R.string.sns_soundcloud
            else -> throw Exception("Invalid sns type.")
        }
        view.setText(stringRes)
    }

    @JvmStatic
    @BindingAdapter("streamerBackground")
    fun setStreamerBackground(view: ConstraintLayout, isSelected: Boolean) {
        val typedValue = TypedValue()
        view.context.theme.resolveAttribute(R.attr.colorPrimaryAlpha, typedValue, true)
        val color = ContextCompat.getColor(view.context, typedValue.resourceId)

        if (isSelected) view.setBackgroundColor(color)
        else view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.transparent))

    }

    @JvmStatic
    @BindingAdapter("chipBackground")
    fun setChipBackground(view: Chip, isSelected: Boolean) {
        val typedValue = TypedValue()
        view.context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)

        if (isSelected) view.setChipBackgroundColorResource(typedValue.resourceId)
        else view.setChipBackgroundColorResource(R.color.Surface)
    }

    @JvmStatic
    @BindingAdapter("networkStatusBarBackground")
    fun setNetworkStatusBarBackground(view: ConstraintLayout, status: NetworkState?) {
        val primaryColorTypedValue = TypedValue()
        view.context.theme.resolveAttribute(R.attr.colorPrimary, primaryColorTypedValue, true)

        val backgroundColorTypedValue = TypedValue()
        view.context.theme.resolveAttribute(R.attr.colorBackground, backgroundColorTypedValue, true)

        if (status == NetworkState.Reconnected) view.setBackgroundResource(primaryColorTypedValue.resourceId)
        else view.setBackgroundResource(backgroundColorTypedValue.resourceId)
    }

    @JvmStatic
    @BindingAdapter("networkStatusBarVisibility")
    fun setNetworkStatusBarVisibility(view: ConstraintLayout, status: NetworkState?) {
        when (status) {
            NetworkState.Reconnected -> {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(RECONNECTION_UI_VISIBLE_TIME)
                    view.visibility = View.GONE
                }
                view.visibility = View.VISIBLE
            }
            NetworkState.NotConnected -> {
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("networkStatusText")
    fun setNetworkStatusText(view: TextView, status: NetworkState?) {
        when (status) {
            NetworkState.Reconnected -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Text))
                view.setText(R.string.network_reconneted)
            }
            NetworkState.NotConnected -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.TextLight))
                view.setText(R.string.network_not_connected)
            }
            else -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Text))
                view.setText(R.string.network_not_connected)
            }
        }
    }
}