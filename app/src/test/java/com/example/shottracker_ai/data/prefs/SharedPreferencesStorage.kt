package com.example.shottracker_ai.data.prefs

import androidx.lifecycle.asFlow
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SharedPreferencesStorageTest {

    private var sharedPreferencesStorage: SharedPreferenceStorage = SharedPreferenceStorage(ApplicationProvider.getApplicationContext())

    @Test
    fun testEmpty() = runBlocking {
        val userName = sharedPreferencesStorage.userNameLiveData.asFlow().first()
        assertEquals(userName, null)
    }

    @Test
    fun testInsert() = runBlocking {
        sharedPreferencesStorage.userName = "Kenny"

        val userName = sharedPreferencesStorage.userNameLiveData.asFlow().first()
        assertEquals(userName, "Kenny")
    }

    @Test
    fun testClear() = runBlocking {
        sharedPreferencesStorage.userName = "Kenny"
        sharedPreferencesStorage.clearAll()
        assertEquals(sharedPreferencesStorage.userName, null)
    }

}