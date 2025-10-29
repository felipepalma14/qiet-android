package com.motoacademy.android.qiet.ui.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))
            /*
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = "ssss",
                                onValueChange = { },
                                placeholder = { Text("Ex: Horário de trabalho") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )

                            IconButton(
                                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primary,RoundedCornerShape(8.dp)),
                                onClick = {
                                    onItemAdded(BlockedList(emptyList()))
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar prefixo")
                            }
                        }
            */
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

@Preview(showBackground = true)
@Composable
private fun BlockerRuleCardPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        BlockerRuleCard(
            title = "Select Contact to Block",
            onItemAdded = {}
        )
    }
}