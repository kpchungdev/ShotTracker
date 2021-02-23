package com.example.shottracker_ai.di

import com.example.shottracker_ai.domain.service.FileManager
import com.example.shottracker_ai.domain.service.StatAnalyzer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)


    @Provides
    fun provideFileSaver(): FileManager {
        return FileManager()
    }

    @Provides
    fun provideStatAnalyzer(): StatAnalyzer {
        return StatAnalyzer()
    }

}