package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motoacademy.android.qiet.ui.components.list.BlockRule
import com.motoacademy.android.qiet.ui.components.list.BlockRuleItem

@Composable
fun BlockDashboardScreen() {

    val state = remember {
        mutableStateListOf(
            BlockRule(
                id = "1",
                title = "Regra de Bloqueio #1",
                isChecked = true,
                blockInterval = "Bloqueio: 08:00 - 18:00",
                blockedContactLabel = "2 Contatos bloqueados",
                prefixLabel = "Prefixos: 3030, 4040"
            ),
            BlockRule(
                id = "2",
                title = "Regra de Bloqueio #2",
                isChecked = false,
                blockInterval = "Bloqueio: 09:00 - 20:00",
                blockedContactLabel = "3 Contatos bloqueados",
                prefixLabel = "Prefixos: 9090, 8080"
            ),
            BlockRule(
                id = "3",
                title = "Regra de Bloqueio #3",
                isChecked = true,
                blockInterval = "Bloqueio: 07:00 - 22:00",
                blockedContactLabel = "1 Contato bloqueado",
                prefixLabel = "Prefixos: 7070"
            ),
            BlockRule(
                id = "4",
                title = "Regra de Bloqueio #4",
                isChecked = false,
                blockInterval = "Bloqueio: 10:00 - 23:00",
                blockedContactLabel = "5 Contatos bloqueados",
                prefixLabel = "Prefixos: 6060, 5050"
            ),
            BlockRule(
                id = "5",
                title = "Regra de Bloqueio #5",
                isChecked = true,
                blockInterval = "Bloqueio: 06:00 - 18:00",
                blockedContactLabel = "4 Contatos bloqueados",
                prefixLabel = "Prefixos: 9999, 3333"
            ),
            BlockRule(
                id = "6",
                title = "Regra de Bloqueio #6",
                isChecked = false,
                blockInterval = "Bloqueio: 00:00 - 08:00",
                blockedContactLabel = "Nenhum contato bloqueado",
                prefixLabel = "Prefixos: 1234, 5678"
            )
        )
    }

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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(
                items = state,
                key = { it.id }
            ) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        BlockRuleItem(
                            modifier = Modifier.fillMaxWidth(),
                            data = item,
                            onClick = { },
                            onCheckedChange = { isChecked ->
                                val index = state.indexOfFirst { it.id == item.id }
                                if (index != -1) {
                                    state[index] = state[index].copy(isChecked = isChecked)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
