package com.example.shottracker_ai.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class SharedPreferenceStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences by lazy { // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
    }

    val userNameLiveData: LiveData<String?> = prefs.asStringLiveData(PREFS_USER_NAME, null)
    val userImagePathLiveData: LiveData<Uri?> = prefs.asUriLiveData(PREFS_USER_IMAGE_PATH, null)

    var userName: String?
            by StringPreference(prefs, PREFS_USER_NAME)


    var userImagePath: Uri?
            by UriPreference(prefs, PREFS_USER_IMAGE_PATH)

    var totalMadeShots: Int
            by IntPreference(prefs, PREFS_TOTAL_MADE_SHOTS)

    var totalShotAttempts: Int
            by IntPreference(prefs, PREFS_TOTAL_SHOT_ATTEMPTS)

    fun clearAll() = prefs.edit().clear().apply()

    companion object {
        const val PREFS_NAME = "shotTracker"
        const val PREFS_USER_NAME = "user_name"
        const val PREFS_USER_IMAGE_PATH = "user_image_path"
        const val PREFS_TOTAL_MADE_SHOTS = "total_made_shots"
        const val PREFS_TOTAL_SHOT_ATTEMPTS = "total_shot_attempts"
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

class IntPreference(
        private val preferences: SharedPreferences,
        private val key: String,
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.getInt(key, 0)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.edit { putInt(key, value) }
    }
}

class UriPreference(
    private val preferences: SharedPreferences,
    private val key: String,
) : ReadWriteProperty<Any, Uri?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Uri? {
        return preferences.getString(key, null)?.toUri()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Uri?) {
        preferences.edit { putString(key, value.toString()) }
    }
}