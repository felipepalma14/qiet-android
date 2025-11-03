package com.motoacademy.android.qiet.features.call_history.presentation

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun CallHistoryScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val screenScroll = rememberScrollState()


    val viewModel: CallHistoryViewModel = hiltViewModel()

    val daily by viewModel.dailyBlockedCallStatus.collectAsStateWithLifecycle()
    val blockedCallsStatus by viewModel.blockedCallStatus.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadBlockedCalls()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .scrollable(state = screenScroll, orientation = Orientation.Vertical)
    ) {
        Text(
            modifier = Modifier,
            text = "Histórico de Bloqueio",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatsCard(title = "Diário", value = daily.toString())
            StatsCard(title = "Semanal", value = "15")
            StatsCard(title = "Mensal", value = "230")
        }

        Spacer(modifier = Modifier.size(24.dp))

        SearchFilterBar(
            modifier = Modifier.fillMaxWidth(),
            onSearch = {

            },
            onFilterSelected = {
                Toast.makeText(context, "Filtro clicado", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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