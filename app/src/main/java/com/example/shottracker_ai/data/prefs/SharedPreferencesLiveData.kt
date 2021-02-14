package com.example.shottracker_ai.data.prefs

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

// https://stackoverflow.com/questions/50649014/livedata-with-shared-preferences

abstract class SharedPreferenceLiveData<T>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defValue: T?
) : LiveData<T?>() {

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (this@SharedPreferenceLiveData.key == key) {
                setValue(getValueFromPreferences(key, defValue))
            }
        }

    abstract fun getValueFromPreferences(key: String?, defValue: T?): T?

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(key, defValue)
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }

}

class SharedPreferencesStringLiveData(private val prefs: SharedPreferences,
                                      key: String,
                                      defaultValue: String? = null
): SharedPreferenceLiveData<String>(prefs, key, defaultValue) {
    override fun getValueFromPreferences(key: String?, defValue: String?): String? {
        return prefs.getString(key, defValue)
    }
}

fun SharedPreferences.asStringLiveData(key: String, defaultValue: String? = null) = SharedPreferencesStringLiveData(this, key, defaultValue)
