package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.motoacademy.android.qiet.ui.components.list.BlockRuleItem

@Composable
fun BlockDashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: BlockDashboardViewModel = hiltViewModel()
    val scrollState = rememberLazyListState()

    val listState by viewModel.rules.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Regras de Bloqueio",
            fontSize = 18.sp
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
        ) {
            items(
                items = listState,
                key = { it.id }
            ) { item ->
                BlockRuleItem(
                    modifier = Modifier.padding(8.dp),
                    data = item,
                    onCheckedChange = { isChecked ->
                        viewModel.onRuleEnabledChange(item.id, isChecked)
                    },
                    onClick = {

                    }
                )
            }
        }
    }
}