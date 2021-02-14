package com.example.shottracker_ai.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    val userName: String?
    val userImagePath: String?
}

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class SharedPreferenceStorage @Inject constructor(
    @ApplicationContext context: Context
) : PreferenceStorage {

    private val prefs: SharedPreferences by lazy { // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
    }

    val userNameLiveData: LiveData<String?> = prefs.asStringLiveData(PREFS_USER_NAME, null)
    val userImagePathLiveData: LiveData<String?> = prefs.asStringLiveData(PREFS_USER_IMAGE_PATH, null)

    override var userName: String?
            by StringPreference(prefs, PREFS_USER_NAME)


    override var userImagePath: String?
            by StringPreference(prefs, PREFS_USER_IMAGE_PATH)

    fun clearAll() = prefs.edit().clear().apply()

    companion object {
        const val PREFS_NAME = "shotTracker"
        const val PREFS_USER_NAME = "user_name"
        const val PREFS_USER_IMAGE_PATH = "user_image_path"
    }

}

class StringPreference(
    private val preferences: SharedPreferences,
    private val key: String,
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.getString(key, null)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.edit { putString(key, value) }
    }
}
