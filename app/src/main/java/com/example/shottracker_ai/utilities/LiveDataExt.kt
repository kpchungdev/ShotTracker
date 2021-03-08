package com.example.shottracker_ai.utilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, K, R> LiveData<T>.combineWith(
        liveData: LiveData<K>,
        block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}


fun <T, K, R> LiveData<T>.combineWithLatest(
        liveData: LiveData<K>,
        block: (T, K) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    var latestKValue: K? = null
    var latestTValue: T? = null

    result.addSource(this) {
        latestTValue = it
        if (latestKValue != null && latestTValue != null) {
            result.value = block(latestTValue!!, latestKValue!!)
        }
    }
    result.addSource(liveData) {
        latestKValue = it
        if (latestKValue != null && latestTValue != null) {
            result.value = block(latestTValue!!, latestKValue!!)
        }
    }

    return result
}

fun <T, K, V, R> LiveData<T>.combineWithLatest(
        liveDataK: LiveData<K>,
        liveDataV: LiveData<V>,
        block: (T, K, V) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    var latestKValue: K? = null
    var latestTValue: T? = null
    var latestVValue: V? = null

    result.addSource(this) {
        latestTValue = it
        if (latestKValue != null && latestTValue != null && latestVValue != null) {
            result.value = block(latestTValue!!, latestKValue!!, latestVValue!!)
        }
    }
    result.addSource(liveDataK) {
        latestKValue = it
        if (latestKValue != null && latestTValue != null && latestVValue != null) {
            result.value = block(latestTValue!!, latestKValue!!, latestVValue!!)
        }
    }
    result.addSource(liveDataV) {
        latestVValue = it
        if (latestKValue != null && latestTValue != null && latestVValue != null) {
            result.value = block(latestTValue!!, latestKValue!!, latestVValue!!)
        }
    }

    return result
}
