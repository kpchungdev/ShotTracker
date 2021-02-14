package com.example.shottracker_ai.data.performance

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shottracker_ai.MainCoroutineRule
import com.example.shottracker_ai.data.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.*
import org.junit.runner.RunWith
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PerformancesDaoTest {

    private lateinit var database: AppDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun checkEmptyDatabase() = runBlockingTest {
        val list: List<Performance> = database.performanceDao().getPerformances().first()
        Assert.assertEquals(list.size, 0)
    }

    @Test
    fun insertPerformance() = runBlockingTest {
        val createdLocalDateTime = LocalDateTime.now()
        val performance = Performance(
            createdTime = createdLocalDateTime,
            shotsMade = 10,
            shotAttempts = 30,
            duration = 50
        )
        database.performanceDao().insertPerformance(performance)

        val loaded = database.performanceDao().getPerformance(performance.localCreatedDate).first()

        Assert.assertThat<Performance>(loaded as Performance, notNullValue())
        Assert.assertEquals(loaded.localCreatedDate.toEpochSecond(), performance.localCreatedDate.toEpochSecond())
        Assert.assertEquals(loaded.shotsMade, performance.shotsMade)
        Assert.assertEquals(loaded.shotAttempts, performance.shotAttempts)
        Assert.assertEquals(loaded.duration, performance.duration)

        val list: List<Performance> = database.performanceDao().getPerformances().first()
        Assert.assertEquals(list.size, 1)
    }

}