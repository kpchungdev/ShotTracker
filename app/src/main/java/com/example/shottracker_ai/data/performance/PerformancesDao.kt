package com.example.shottracker_ai.data.performance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface PerformancesDao {
    @Query("SELECT * FROM performances")
    fun getPerformances(): Flow<List<Performance>>

    @Query("SELECT * FROM performances WHERE utc_zoned_date_time = :zonedDateTime")
    fun getPerformance(zonedDateTime: ZonedDateTime): Flow<Performance>

    @Insert
    suspend fun insertPerformance(performance: Performance): Long
}