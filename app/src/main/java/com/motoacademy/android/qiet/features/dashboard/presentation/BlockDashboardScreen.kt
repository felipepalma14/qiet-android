package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    //bloqueio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Regras de Bloqueio",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
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
