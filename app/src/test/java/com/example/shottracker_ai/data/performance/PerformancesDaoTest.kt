package com.example.shottracker_ai.data.performance

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shottracker_ai.MainCoroutineRule
import com.example.shottracker_ai.data.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
                duration = 50,
                totalFieldGoal = 10.toDouble() / 30.toDouble()
        )
        database.performanceDao().insertPerformance(performance)

        val loaded = database.performanceDao().getPerformance(performance.localCreatedDate).first()

        Assert.assertThat(loaded, notNullValue())
        Assert.assertEquals(loaded.localCreatedDate.toEpochSecond(), performance.localCreatedDate.toEpochSecond())
        Assert.assertEquals(loaded.shotsMade, performance.shotsMade)
        Assert.assertEquals(loaded.shotAttempts, performance.shotAttempts)
        Assert.assertEquals(loaded.duration, performance.duration)

        val list: List<Performance> = database.performanceDao().getPerformances().first()
        Assert.assertEquals(list.size, 1)
    }

    @Test
    fun testOrder() = runBlocking {
        val performance1 = Performance(
                createdTime = LocalDateTime.now().plusDays(3),
                shotsMade = 1,
                shotAttempts = 10,
                duration = 10,
                totalFieldGoal = 1.toDouble() / 10.toDouble()
        )

        val performance2 = Performance(
                createdTime = LocalDateTime.now().plusDays(1),
                shotsMade = 2,
                shotAttempts = 10,
                duration = 10,
                totalFieldGoal = 3.toDouble() / 20.toDouble()
        )

        val performance3 = Performance(
                createdTime = LocalDateTime.now().plusDays(2),
                shotsMade = 3,
                shotAttempts = 10,
                duration = 10,
                totalFieldGoal = 6.toDouble() / 30.toDouble()
        )

        database.performanceDao().insertPerformance(performance1)
        database.performanceDao().insertPerformance(performance2)
        database.performanceDao().insertPerformance(performance3)

        val performances = database.performanceDao().getPerformances().first()

        Assert.assertEquals(performance2.shotsMade, performances[0].shotsMade)
        Assert.assertEquals(performance3.shotsMade, performances[1].shotsMade)
        Assert.assertEquals(performance1.shotsMade, performances[2].shotsMade)
    }

}