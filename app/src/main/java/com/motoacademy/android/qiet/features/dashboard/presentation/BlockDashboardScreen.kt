package com.motoacademy.android.qiet.features.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.motoacademy.android.qiet.ui.components.list.BlockRuleItem
import kotlinx.coroutines.launch

@Composable
fun BlockDashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: BlockDashboardViewModel = hiltViewModel()
    val scrollState = rememberLazyListState()
    val listState by viewModel.rules.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var ruleToDeleteId by remember { mutableStateOf<Long?>(null) }
    var ruleToDeleteName by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isDeleting) {
                    showDeleteDialog = false
                    ruleToDeleteId = null
                    ruleToDeleteName = ""
                }
            },
            title = { Text("Excluir Regra") },
            text = {
                Column {
                    Text("Tem certeza que deseja excluir a regra?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "\"$ruleToDeleteName\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red.copy(alpha = 0.8f)
                    )

                    if (isDeleting) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Excluindo...", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            },
            confirmButton = {
                if (isDeleting) {
                    TextButton(onClick = {}, enabled = false) {
                        Text("Excluindo...", color = Color.Gray)
                    }
                } else {
                    TextButton(
                        onClick = {
                            isDeleting = true
                            coroutineScope.launch {
                                ruleToDeleteId?.let { id ->
                                    viewModel.deleteRule(id)
                                    isDeleting = false
                                    showDeleteDialog = false
                                    ruleToDeleteId = null
                                    ruleToDeleteName = ""
                                }
                            }
                        }
                    ) {
                        Text("EXCLUIR", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (!isDeleting) {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            ruleToDeleteId = null
                            ruleToDeleteName = ""
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    BlockRuleItem(
                        modifier = Modifier.fillMaxWidth(),
                        data = item,
                        onCheckedChange = { isChecked ->
                            viewModel.onRuleEnabledChange(item.id, isChecked)
                        },
                        onClick = {
                        }
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate("edit_rule/${item.id}")
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar Regra",
                                tint = Color(0xFF1976D2), // Azul
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))


                        IconButton(
                            onClick = {
                                ruleToDeleteName = item.title
                                ruleToDeleteId = item.id
                                showDeleteDialog = true
                                isDeleting = false
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir Regra",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}