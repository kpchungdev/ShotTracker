package com.example.shottracker_ai.di

import android.content.Context
import com.example.shottracker_ai.data.AppDatabase
import com.example.shottracker_ai.data.performance.PerformancesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePerformanceDao(appDatabase: AppDatabase): PerformancesDao {
        return appDatabase.performanceDao()
    }

}