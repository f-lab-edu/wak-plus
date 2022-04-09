package com.june0122.wakplus.utils

import com.june0122.wakplus.utils.Language.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

/**
 * 명수법(命數法)
 *
 * 한자 숫자는 1만까지는 1진법과 10진법이 혼용되어 있으나, 1만부터는 네 자리마다 수를 끊는 만진법(萬進法)이 응용
 * 영어 숫자는 세 자리마다 수를 끊는 천진법(千進法) 사용
 */
private const val SUFFIX_EN = "kMBTPE"
private const val SUFFIX_KR = "천만억조"
private const val SUFFIX_JP = "万億兆"
private const val THREE_DIGITS = 1_000.0
private const val FOUR_DIGITS = 10_000.0

fun String.withSuffix(language: Language): String {
    val viewCount = this.toLong()

    return when (language) {
        KR -> {
            if (viewCount < 1_000) return viewCount.toString()

            if (viewCount in 1_000 until 10_000) {
                val number = if ((viewCount.toInt() % 1_000) / 100 == 0) {
                    (viewCount.toInt() / 1_000).toString()
                } else {
                    (viewCount.toDouble() / 1_000).toString()
                }
                return String.format("%s%c", number, SUFFIX_KR[0])
            }

            val exp = (log10(viewCount.toDouble()) / log10(FOUR_DIGITS)).toInt()
            val format = DecimalFormat("0.#")
            val integer = (viewCount / (FOUR_DIGITS).pow(exp)).toInt()
            val number = if (integer >= 10) integer.toString() else format.format(viewCount / (FOUR_DIGITS).pow(exp))

            String.format("%s%c", number, SUFFIX_KR[exp])
        }

        JP -> {
            if (viewCount < 10_000) return viewCount.toString()

            val exp = (log10(viewCount.toDouble()) / log10(FOUR_DIGITS)).toInt()
            val format = DecimalFormat("0.#")
            val integer = (viewCount / (FOUR_DIGITS).pow(exp)).toInt()
            val number = if (integer >= 10) integer.toString() else format.format(viewCount / (FOUR_DIGITS).pow(exp))

            String.format("%s%c", number, SUFFIX_JP[exp - 1])
        }

        EN -> {
            if (viewCount < 1_000) return viewCount.toString()

            val exp = (log10(viewCount.toDouble()) / log10(THREE_DIGITS)).toInt()
            val format = DecimalFormat("0.#")
            val integer = (viewCount / (THREE_DIGITS).pow(exp)).toInt()
            val number = if (integer >= 10) integer.toString() else format.format(viewCount / (THREE_DIGITS).pow(exp))

            String.format("%s%c", number, SUFFIX_EN[exp - 1])
        }
    }
}


/**
 * 콘텐츠의 publishedTime을 통해 지난 시간 표시
 */
private const val SECOND_MILLIS = 1000L // Integer Overflow 방지를 위해 Long으로 처리
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val WEEK_MILLIS = 7 * DAY_MILLIS
private const val MONTH_MILLIS = 31 * DAY_MILLIS
private const val YEAR_MILLIS = 12 * MONTH_MILLIS

private fun currentDate(): Date {
    val calendar = Calendar.getInstance()
    return calendar.time
}

// TODO: 언어를 영어로 설정 시에만 'a minute ago', 'an hour ago' 케이스 추가
fun String.timeAgo(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val date = inputFormat.parse(this) as Date
    var time = date.time
    if (time < 1_000_000_000_000L) time *= 1_000
    val now = currentDate().time
    if (time > now || time <= 0) return "in the future"
    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> "moments ago"
//        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
//        diff < 2 * HOUR_MILLIS -> "an hour ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        diff < WEEK_MILLIS -> "${diff / DAY_MILLIS} days ago"
        diff < MONTH_MILLIS -> "${diff / WEEK_MILLIS} weeks ago"
        diff < YEAR_MILLIS -> "${diff / MONTH_MILLIS} months ago"
        else -> "${diff / YEAR_MILLIS} years ago"
    }
}