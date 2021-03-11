package com.example.shottracker_ai.data.performance

import androidx.room.*
import com.example.shottracker_ai.ui.common.chart.ChartData
import com.example.shottracker_ai.utilities.toLocalZonedDateTime
import com.example.shottracker_ai.utilities.toUTCZonedDateTime
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity(tableName = "performances")
data class Performance(
        @PrimaryKey @ColumnInfo(name = "utc_zoned_date_time") val utcCreatedDate: ZonedDateTime,
        @ColumnInfo(name = "shots_made") val shotsMade: Int,
        @ColumnInfo(name = "shot_attempts") val shotAttempts: Int,
        @ColumnInfo(name = "duration_minutes") val duration: Int,
        @ColumnInfo(name = "total_field_goal") val totalFieldGoal: Float,
) {

    constructor(createdTime: LocalDateTime,
                shotsMade: Int,
                shotAttempts: Int,
                duration: Int,
                totalFieldGoal: Float) : this(
            utcCreatedDate = createdTime.toUTCZonedDateTime(),
            shotsMade = shotsMade,
            shotAttempts = shotAttempts,
            duration = duration,
            totalFieldGoal = totalFieldGoal
    )

    val localCreatedDate: ZonedDateTime
        get() = utcCreatedDate.toLocalZonedDateTime()

}