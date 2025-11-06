package com.motoacademy.android.qiet.features.call_history.presentation

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockRulesUseCase
import com.motoacademy.android.qiet.domain.usecase.GetAllBlockedCallsUseCase
import com.motoacademy.android.qiet.domain.usecase.GetDailyBlockedCountUseCase
import com.motoacademy.android.qiet.features.call_history.model.BlockedCallSpamUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallHistoryViewModel @Inject constructor(
    private val getAllBlockedCallsUseCase: GetAllBlockedCallsUseCase,
    private val getDailyBlockedCountUseCase: GetDailyBlockedCountUseCase,
    private val getAllBlockRulesUseCase: GetAllBlockRulesUseCase
) : ViewModel() {
    private val _dailyBlockedCallStatus = MutableStateFlow<Int>(0)
    val dailyBlockedCallStatus: StateFlow<Int> = _dailyBlockedCallStatus.asStateFlow()

    private val _blockRulesStatus = MutableStateFlow<List<BlockRuleEntity>>(emptyList())
    val blockRulesStatus: StateFlow<List<BlockRuleEntity>> = _blockRulesStatus.asStateFlow()

    private val _blockedCallStatus = MutableStateFlow<List<BlockedCallSpamUi>>(emptyList())
    val blockedCallStatus: StateFlow<List<BlockedCallSpamUi>> = _blockedCallStatus.asStateFlow()

    fun loadBlockedCalls() {
        viewModelScope.launch {
            launch {
                getDailyBlockedCountUseCase().collect { dailyBlockedCall ->
                    _dailyBlockedCallStatus.value = dailyBlockedCall.count()
                }
            }

            launch {
                getAllBlockedCallsUseCase().collect { blockedCalls ->
                    _blockedCallStatus.value = blockedCalls
                }
            }

            launch {
                getAllBlockRulesUseCase().collect { blockRules ->
                    _blockRulesStatus.value = blockRules
                }
            }
        }
    }

    fun filter(ruleName: String) {
        viewModelScope.launch {
            getAllBlockedCallsUseCase().collect { items ->
                _blockedCallStatus.value = items.filter { it.reason == ruleName }
            }
        }
    }

}