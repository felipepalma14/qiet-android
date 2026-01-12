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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
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

    private val filterState = MutableStateFlow("")

    val blockedCallsUiState = combine(
        getAllBlockedCallsUseCase(),
        filterState
    ) { calls, filter ->
        calls.filter { call ->
            val phoneMatch =
                filter.isBlank() ||
                        call.number.contains(filter, ignoreCase = true)

            val reasonMatch =
                filter.isBlank() ||
                        call.reason.contains(filter, ignoreCase = true)

            phoneMatch || reasonMatch
        }
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    fun loadBlockedCalls() {
        viewModelScope.launch {
            launch {
                getDailyBlockedCountUseCase().collect { dailyBlockedCall ->
                    _dailyBlockedCallStatus.value = dailyBlockedCall.count()
                }
            }

//            launch {
//                getAllBlockedCallsUseCase().collect { blockedCalls ->
//                    _blockedCallStatus.value = blockedCalls
//                }
//            }

            launch {
                getAllBlockRulesUseCase().collect { blockRules ->
                    _blockRulesStatus.value = blockRules
                }
            }
        }
    }

    fun filter(ruleName: String) {
        viewModelScope.launch {
            filterState.value = ruleName
        }
    }

}