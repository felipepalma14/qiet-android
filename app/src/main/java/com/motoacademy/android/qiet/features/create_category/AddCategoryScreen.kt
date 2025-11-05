package com.motoacademy.android.qiet.features.create_category

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.motoacademy.android.qiet.ui.components.card.AddRuleNameCard
import com.motoacademy.android.qiet.ui.components.card.SelectRuleColorCard
import com.motoacademy.android.qiet.ui.theme.BlueCategory
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    navController: NavController
) {
    val screenScroll = rememberScrollState()

    var ruleName by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf(BlueCategory) }

    // Prefixos de bloqueio
    var prefixInput by remember { mutableStateOf("") }
    val prefixList = remember { mutableStateListOf<String>() }

    // restrição de horário
    var timeRestrictionEnabled by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("22:00") }

    val daysOfWeek = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
    val selectedDays = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(screenScroll)
    ) {
        Text(
            text = "Nova regra",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.size(16.dp))

        AddRuleNameCard(
            ruleName = ruleName,
            isEnabled = isEnabled,
            onNameChange = { ruleName = it },
            onToggleChange = { isEnabled = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        SelectRuleColorCard(
            title = "Cor da regra",
            color = color,
            onColorSelected = { color = it }
        )

        Spacer(Modifier.height(16.dp))

        // Prefixos de bloqueio
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Prefixo para bloquear",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Ex: 0800, 11, +55...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = prefixInput,
                        onValueChange = { prefixInput = it },
                        placeholder = { Text("Digite um prefixo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (prefixInput.isNotBlank()) {
                                prefixList.add(prefixInput.trim())
                                prefixInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color(0xFFEEEEEE),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar",
                            tint = Color.Black
                        )
                    }
                }

                // Lista de prefixos
                prefixList.forEach { prefix ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "- $prefix",
                            color = Color(0xFFD32F2F),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        // Ícone de remover usando X
                        IconButton(onClick = { prefixList.remove(prefix) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remover",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Restrição de horário
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Restrições de horário",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = timeRestrictionEnabled,
                        onCheckedChange = { timeRestrictionEnabled = it }
                    )
                }

                if (timeRestrictionEnabled) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TimePickerButton("Início", startTime) { startTime = it }
                        TimePickerButton("Fim", endTime) { endTime = it }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Dias da semana",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(8.dp))

                    Column {
                        for (weekRow in daysOfWeek.chunked(4)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                weekRow.forEach { day ->
                                    val isSelected = selectedDays.contains(day)
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .weight(1f)
                                            .background(
                                                if (isSelected)
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                                                else Color(0xFFE0E0E0),
                                                shape = RoundedCornerShape(50)
                                            )
                                            .clickable {
                                                if (isSelected)
                                                    selectedDays.remove(day)
                                                else
                                                    selectedDays.add(day)
                                            }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day,
                                            color = if (isSelected) Color.White else Color.Black,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Botões Criar e Cancelar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { navController.navigate("block_rules_screen") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    println("Prefixos: ${prefixList.joinToString()}")
                    println("Horário: $startTime - $endTime")
                    println("Dias selecionados: ${selectedDays.joinToString()}")
                    navController.navigate("screen")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Criar")
            }
        }
    }
}

@Composable
fun TimePickerButton(label: String, time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, fontSize = 14.sp)
        OutlinedButton(
            onClick = {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        val formattedTime =
                            String.format("%02d:%02d", selectedHour, selectedMinute)
                        onTimeSelected(formattedTime)
                    },
                    hour,
                    minute,
                    true
                ).show()
            },
            modifier = Modifier.width(120.dp),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            Text(
                text = time,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
