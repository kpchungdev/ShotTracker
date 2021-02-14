package com.example.shottracker_ai.data.performance

import androidx.room.*
import com.example.shottracker_ai.utilities.toLocalZonedDateTime
import com.example.shottracker_ai.utilities.toUTCZonedDateTime
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity(tableName = "performances")
data class Performance(
    @PrimaryKey @ColumnInfo(name = "utc_zoned_date_time") val utcCreatedDate: ZonedDateTime,
    @ColumnInfo(name = "shots_made") val shotsMade: Int,
    @ColumnInfo(name = "shot_attempts") val shotAttempts: Int,
    @ColumnInfo(name = "duration_minutes") val duration: Int
) {

    constructor(createdTime: LocalDateTime,
                shotsMade: Int,
                shotAttempts: Int,
                duration: Int) : this(
        utcCreatedDate = createdTime.toUTCZonedDateTime(),
        shotsMade = shotsMade,
        shotAttempts = shotAttempts,
        duration = duration
    )

    val localCreatedDate: ZonedDateTime
        get() = utcCreatedDate.toLocalZonedDateTime()

}