package com.motoacademy.android.qiet.di

import com.motoacademy.android.qiet.domain.repository.BlockRuleRepository
import com.motoacademy.android.qiet.domain.usecase.AddBlockRuleUseCase
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockRulesUseCase
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockedCallsUseCase
import com.motoacademy.android.qiet.domain.usecase.GetDailyBlockedCountUseCase
import com.motoacademy.android.qiet.domain.usecase.UpdateRuleEnabledUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn(dagger.hilt.components.SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideGetAllBlockRulesUseCase(repository: BlockRuleRepository): GetAllBlockRulesUseCase {
        return GetAllBlockRulesUseCase(repository)
    }

    @Provides
    fun provideUpdateRuleEnabledUseCase(repository: BlockRuleRepository): UpdateRuleEnabledUseCase {
        return UpdateRuleEnabledUseCase(repository)
    }

    @Provides
    fun provideGetDailyBlockedCountUseCase(repository: BlockRuleRepository): GetDailyBlockedCountUseCase {
        return GetDailyBlockedCountUseCase(repository)
    }

    @Provides
    fun provideGetAllBlockedCallsUseCase(repository: BlockRuleRepository): GetAllBlockedCallsUseCase {
        return GetAllBlockedCallsUseCase(repository)
    }

    @Provides
    fun provideAddBlockRuleUseCase(repository: BlockRuleRepository): AddBlockRuleUseCase {
        return AddBlockRuleUseCase(repository)
    }
}
