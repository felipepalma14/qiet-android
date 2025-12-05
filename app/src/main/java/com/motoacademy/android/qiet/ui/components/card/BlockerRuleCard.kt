package com.motoacademy.android.qiet.ui.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class BlockedContent(
    val id: Int,
    val regex: String?,
    val contact: String?,
)

@Immutable
data class BlockedList(
    val blockedContents: List<BlockedContent>,
)

@Composable
fun BlockerRuleCard(
    title: String,
    onItemAdded: (List<String>) -> Unit,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    var regras by remember { mutableStateOf(listOf("")) }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Linha do título com botões de ação
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // Botões de ação
                Row {
                    // Botão Editar
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar regra",
                            tint = Color.Blue
                        )
                    }

                    // Botão Excluir
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir regra",
                            tint = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            regras.forEachIndexed { index, regra ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = regra,
                        onValueChange = { novoValor ->
                            regras = regras.toMutableList().also {
                                it[index] = novoValor
                            }
                            onItemAdded(regras)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Digite a regra...") }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botão + só aparece na primeira caixa de texto
                    if (index == 0) {
                        IconButton(onClick = {
                            regras = regras + ""
                            onItemAdded(regras)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar regra"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Versão alternativa com Switch e botões na parte inferior
@Composable
fun BlockerRuleCardWithSwitch(
    title: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onItemAdded: (List<String>) -> Unit,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    var regras by remember { mutableStateOf(listOf("")) }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Linha do título com Switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = isEnabled,
                    onCheckedChange = onToggle
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            regras.forEachIndexed { index, regra ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = regra,
                        onValueChange = { novoValor ->
                            regras = regras.toMutableList().also {
                                it[index] = novoValor
                            }
                            onItemAdded(regras)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Digite a regra...") }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botão + só aparece na primeira caixa de texto
                    if (index == 0) {
                        IconButton(onClick = {
                            regras = regras + ""
                            onItemAdded(regras)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar regra"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botões de ação na parte inferior
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botão Editar
                TextButton(
                    onClick = onEdit
                ) {
                    Text("Editar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botão Excluir
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}

// Versão SIMPLES para cards de lista (como no dashboard)
@Composable
fun SimpleRuleCard(
    title: String,
    description: String = "",
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Linha 1: Título e Switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Switch(
                    checked = isEnabled,
                    onCheckedChange = onToggle
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Linha 2: Botões de ação
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botão Editar
                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botão Excluir
                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockerRuleCardPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Preview do card original
        BlockerRuleCard(
            title = "Select Contact to Block",
            onItemAdded = {},
            onEdit = { println("Editar clicado") },
            onDelete = { println("Excluir clicado") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preview do card com switch
        BlockerRuleCardWithSwitch(
            title = "Bloqueio de Spam",
            isEnabled = true,
            onToggle = {},
            onItemAdded = {},
            onEdit = { println("Editar clicado") },
            onDelete = { println("Excluir clicado") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preview do card simples
        SimpleRuleCard(
            title = "Bloqueio de Telemarketing",
            description = "3 prefixos • 09:00-22:00",
            isEnabled = false,
            onToggle = {},
            onEdit = { println("Editar clicado") },
            onDelete = { println("Excluir clicado") }
        )
    }
}