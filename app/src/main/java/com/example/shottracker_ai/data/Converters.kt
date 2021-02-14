package com.example.shottracker_ai.data


import androidx.room.TypeConverter
import java.time.*

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {

    @TypeConverter fun zoneLocalDateTimeToDatestamp(zonedDateTime: ZonedDateTime): Long = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()

    @TypeConverter fun datestampToZoneLocalDateTime(value: Long): ZonedDateTime {
        val instant = Instant.ofEpochSecond(value)
        return ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
    }
}
