package com.motoacademy.android.qiet.features.dashboard.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
            title = {
                Text(
                    "Excluir Regra",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "Tem certeza que deseja excluir a regra?",
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "\"$ruleToDeleteName\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFFD32F2F).copy(alpha = 0.9f)
                    )

                    if (isDeleting) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFD32F2F)
                        )

                    }
                }
            },
            confirmButton = {
                if (isDeleting) {
                    TextButton(onClick = {}, enabled = false) {
                        Text("Excluindo...", color = Color(0xFF999999))
                    }
                } else {
                    FilledTonalButton(
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
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFFD32F2F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("EXCLUIR", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (!isDeleting) {
                    OutlinedButton(
                        onClick = {
                            showDeleteDialog = false
                            ruleToDeleteId = null
                            ruleToDeleteName = ""
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF666666)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = null
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Medium)
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                text = "Regras de Bloqueio",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Lista de regras
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (listState.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "📱",
                                fontSize = 48.sp
                            )

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD32F2F),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(
                                    "Criar Primeira Regra",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                items(
                    items = listState,
                    key = { it.id }
                ) { item ->
                    BlockRuleItem(
                        modifier = Modifier.fillMaxWidth(),
                        data = item,
                        onCheckedChange = { isChecked ->
                            viewModel.onRuleEnabledChange(item.id, isChecked)
                        },
                        onClick = {},
                        showActions = true,
                        onEditClick = { navController.navigate("edit_rule/${item.id}") },
                        onDeleteClick = {
                            ruleToDeleteName = item.title
                            ruleToDeleteId = item.id
                            showDeleteDialog = true
                            isDeleting = false
                        }
                    )
                }
            }
        }

        // FloatingActionButton
        if (listState.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = { /* criar nova regra */ },
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Adicionar Regra",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
