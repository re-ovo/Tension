package me.rerere.tension.util

import kotlinx.datetime.*

/**
 * 解析 ISO 8601 格式的时间字符串
 */
fun parseIso8601TimeToLocalTime(time: String): String {
    val instant = Instant.parse(time).toLocalDateTime(TimeZone.of("UTC+8"))
    return "${instant.monthNumber}月${instant.dayOfMonth}日 ${instant.hour.toString().padStart(2, '0')}:${instant.minute.toString().padStart(2, '0')}"
}