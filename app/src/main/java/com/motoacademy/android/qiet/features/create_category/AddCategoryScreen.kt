package com.motoacademy.android.qiet.features.create_category

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.motoacademy.android.qiet.ui.components.card.AddRuleNameCard
import com.motoacademy.android.qiet.ui.components.card.BlockerRuleCard
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

    // Estados da restrição de horário
    var timeRestrictionEnabled by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("22:00") }

    // Dias da semana
    val daysOfWeek = listOf(
        "Seg", "Ter", "Qua", "Qui",
        "Sex", "Sáb", "Dom"
    )


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

        BlockerRuleCard(
            title = "Regras de bloqueio",
            onItemAdded = { }
        )

        Spacer(Modifier.height(24.dp))

        // restrição de horário
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
                                                else
                                                    Color(0xFFE0E0E0),
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

        Spacer(modifier = Modifier.height(32.dp))

        // Botões de ação
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
                        val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
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
