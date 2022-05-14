package com.june0122.wakplus.utils

import android.content.Context
import com.june0122.wakplus.R
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
    val format = DecimalFormat("0.#")

    return when (language) {
        KR -> {
            if (viewCount < 1_000) return viewCount.toString()

            if (viewCount in 1_000 until 10_000) {
                val number = if ((viewCount.toInt() % 1_000) / 100 == 0) {
                    (viewCount.toInt() / 1_000).toString()
                } else {
                    format.format(viewCount.toDouble() / 1_000)
                }
                return String.format("%s%c", number, SUFFIX_KR[0])
            }

            val exp = (log10(viewCount.toDouble()) / log10(FOUR_DIGITS)).toInt()
            val integer = (viewCount / (FOUR_DIGITS).pow(exp)).toInt()
            val number = if (integer >= 10) integer.toString() else format.format(viewCount / (FOUR_DIGITS).pow(exp))

            String.format("%s%c", number, SUFFIX_KR[exp])
        }

        JP -> {
            if (viewCount < 10_000) return viewCount.toString()

            val exp = (log10(viewCount.toDouble()) / log10(FOUR_DIGITS)).toInt()
            val integer = (viewCount / (FOUR_DIGITS).pow(exp)).toInt()
            val number = if (integer >= 10) integer.toString() else format.format(viewCount / (FOUR_DIGITS).pow(exp))

            String.format("%s%c", number, SUFFIX_JP[exp - 1])
        }

        EN -> {
            if (viewCount < 1_000) return viewCount.toString()

            val exp = (log10(viewCount.toDouble()) / log10(THREE_DIGITS)).toInt()
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
private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val UTC = "UTC"

private fun currentDate(): Date {
    val calendar = Calendar.getInstance(Locale.getDefault())
    return calendar.time
}

// 한국 시간은 UTC보다 9시간 빠르다.
// ISO 8601 형식에서 'Z'는 UTC 타임존을 의미하므로 타임존을 UTC로 지정해주지 않으면 원하는 시간보다 9시간이 더해진 값이 나온다.
fun String.timeAgo(context: Context): String {
    val currentTime = currentDate().time
    var publishedTime = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        .run {
            timeZone = TimeZone.getTimeZone(UTC)
            parse(this@timeAgo) as Date
        }.time

    if (publishedTime < 1_000_000_000_000L) publishedTime *= 1_000
    if (publishedTime > currentTime || publishedTime <= 0) return context.getString(R.string.published_in_future)

    val diff = currentTime - publishedTime

    return when {
        diff < MINUTE_MILLIS -> context.getString(R.string.moments_ago)
        diff < 2 * MINUTE_MILLIS -> context.getString(R.string.minute_ago)
        diff < 60 * MINUTE_MILLIS -> context.getString(R.string.minutes_ago, diff / MINUTE_MILLIS)
        diff < 2 * HOUR_MILLIS -> context.getString(R.string.hour_ago)
        diff < 24 * HOUR_MILLIS -> context.getString(R.string.hours_ago, diff / HOUR_MILLIS)
        diff < 48 * HOUR_MILLIS -> context.getString(R.string.day_ago)
        diff < 2 * WEEK_MILLIS -> context.getString(R.string.days_ago, diff / DAY_MILLIS)
        diff < MONTH_MILLIS -> context.getString(R.string.weeks_ago, diff / WEEK_MILLIS)
        diff < 2 * MONTH_MILLIS -> context.getString(R.string.month_ago)
        diff < YEAR_MILLIS -> context.getString(R.string.months_ago, diff / MONTH_MILLIS)
        diff < 2 * YEAR_MILLIS -> context.getString(R.string.year_ago)
        else -> context.getString(R.string.years_ago, diff / YEAR_MILLIS)
    }
}