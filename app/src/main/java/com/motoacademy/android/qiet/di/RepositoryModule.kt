package com.motoacademy.android.qiet.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import com.motoacademy.android.qiet.data.local.dao.BlockRuleDao
import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import com.motoacademy.android.qiet.data.repository.BlockRuleRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBlockRuleRepository(
        dao: BlockRuleDao
    ): BlockRuleRepository {
        return BlockRuleRepositoryImpl(dao)
    }
}
