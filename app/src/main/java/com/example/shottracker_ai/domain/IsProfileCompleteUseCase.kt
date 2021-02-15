package com.example.shottracker_ai.domain

import com.example.shottracker_ai.data.prefs.SharedPreferenceStorage
import com.example.shottracker_ai.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class IsProfileCompleteUseCase @Inject constructor(
    private val preferenceStorage: SharedPreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, Boolean>(dispatcher) {
    override suspend fun execute(parameters: Unit): Boolean = !preferenceStorage.userName.isNullOrBlank()
}