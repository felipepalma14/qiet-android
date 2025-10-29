package com.motoacademy.android.qiet.di

import android.content.Context
import androidx.room.Room
import com.motoacademy.android.qiet.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "qiet_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideBlockRuleDao(database: AppDatabase): BlockRuleDao {
        return database.blockRuleDao()
    }
}
