package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.motoacademy.android.qiet.ui.components.list.BlockRule
import com.motoacademy.android.qiet.ui.components.list.BlockRuleItem

@Composable
fun BlockDashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    val scrollState = rememberLazyListState()
    val state = mutableListOf<BlockRule>(
        BlockRule(
            id = "1",
            title = "Regra de Bloqueio #1",
            isChecked = true,
            blockInterval = "Bloqueio: 08:00 - 18:00",
            blockedContactLabel = "2 Contato(s) bloqueado(s)",
        ),
        BlockRule(
            id = "2",
            title = "Regra de Bloqueio #2",
            prefixLabel = "Prefixo(s): 3030, 4040",
            blockInterval = "Bloqueio: 08:00 - 18:00",
        ),
        BlockRule(
            id = "3",
            title = "Regra de Bloqueio #2",
            prefixLabel = "Prefixo(s): 3030, 4040",
            blockInterval = "Bloqueio: 08:00 - 18:00",
        ),        BlockRule(
            id = "4",
            title = "Regra de Bloqueio #2",
            prefixLabel = "Prefixo(s): 3030, 4040",
            blockInterval = "Bloqueio: 08:00 - 18:00",
        ),        BlockRule(
            id = "5",
            title = "Regra de Bloqueio #2",
            prefixLabel = "Prefixo(s): 3030, 4040",
            blockInterval = "Bloqueio: 08:00 - 18:00",
        ),        BlockRule(
            id = "6",
            title = "Regra de Bloqueio #2",
            prefixLabel = "Prefixo(s): 3030, 4040",
            blockInterval = "Bloqueio: 08:00 - 18:00",
        )
    )

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
                items = state,
                key = { it.id }
            ) { item ->
                BlockRuleItem(
                    modifier = Modifier.padding(16.dp),
                    data = item,
                    onCheckedChange = { isChecked ->
                        state.find { it.id == item.id }?.copy(isChecked = isChecked)?.let {
                            val ind = state.indexOf(item)
                            state.add(ind, it)
                        }
                    },
                    onClick = {

                    }
                )
            }
        }
    }
}