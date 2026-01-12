package com.motoacademy.android.qiet.features.call_history.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motoacademy.android.qiet.features.dashboard.presentation.BlockDashboardViewModel
import com.motoacademy.android.qiet.ui.components.card.StatsCard
import com.motoacademy.android.qiet.ui.components.list.BlockRule
import com.motoacademy.android.qiet.ui.components.list.SpamCall
import com.motoacademy.android.qiet.ui.components.list.SpamCallItem
import com.motoacademy.android.qiet.ui.components.search.SearchFilterBar
import com.motoacademy.android.qiet.ui.components.search.SearchFilterBarModel

@Composable
fun CallHistoryScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val screenScroll = rememberScrollState()

    val viewModel: CallHistoryViewModel = hiltViewModel()

    val daily by viewModel.dailyBlockedCallStatus.collectAsStateWithLifecycle()
    val blockedCallsStatus by viewModel.blockedCallsUiState.collectAsStateWithLifecycle(emptyList())
    val blockRulesStatus by viewModel.blockRulesStatus.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadBlockedCalls()
    }

    LaunchedEffect(blockedCallsStatus.size) {
        if (blockedCallsStatus.isNotEmpty()) {
            scrollState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .scrollable(state = screenScroll, orientation = Orientation.Vertical)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFD32F2F),
                            Color(0xFFB71C1C)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Histórico de Bloqueio",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            StatsCard(title = "Diário", value = daily.toString())
            StatsCard(title = "Semanal", value = "15")
            StatsCard(title = "Mensal", value = "230")
        }

        Spacer(modifier = Modifier.size(24.dp))

        SearchFilterBar(
            modifier = Modifier.fillMaxWidth(),
            data = SearchFilterBarModel(
                searchPlaceholder = "Buscar por número",
                filterButtonTitle = "Filter",
                dialogTitle = "Selecione a categoria",
                categoryList = blockRulesStatus
            ),
            onSearch = { rule ->
                viewModel.filter(rule)
            },
            onFilterSelected = {
                Toast.makeText(context, "Filtro clicado", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            state = scrollState,
        ) {
            items(
                items = blockedCallsStatus,
                key = { it.id }
            ) { item ->
                val spamItem = SpamCall(
                    id = item.id,
                    phoneNumber = item.number,
                    status = item.reason,
                    date = item.formattedDate

                )
                SpamCallItem(Modifier.padding(vertical = 8.dp),spamItem)
            }
        }
    }
}

@Preview
@Composable
fun CallHistoryScreenPreview() {
    CallHistoryScreen()
}