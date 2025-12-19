package com.motoacademy.android.qiet.features.create_category

import android.app.TimePickerDialog
import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.local.model.toPtBrLabel
import com.motoacademy.android.qiet.navigation.Screen
import java.util.Calendar


private val PrimaryRed = Color(0xFFD32F2F)
private val DarkRed = Color(0xFFB71C1C)
private val TextGray = Color(0xFF8A8A8A)
private val CardWhite = Color.White
private val BackgroundGray = Color(0xFFF5F5F5)

@Composable
fun AddCategoryScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel: CreateCategoryViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val scroll = rememberScrollState()


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RuleEffect.NavigateBack -> navController.popBackStack()
                is RuleEffect.Success -> navController.navigate(Screen.BlockDashboardScreen)
                is RuleEffect.ShowError ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(scroll)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PrimaryRed, DarkRed)
                    )
                )
                .padding(vertical = 20.dp)
        ) {
            Text(
                text = "Nova Regra",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(Modifier.height(24.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardWhite)
                .padding(20.dp)
        ) {
            Text("Nome da regra", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = state.ruleName,
                onValueChange = { viewModel.onEvent(RuleEvent.OnRuleNameChanged(it)) },
                singleLine = true,
                placeholder = { Text("Ex: Bloquear 0800", color = TextGray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryRed,
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    cursorColor = PrimaryRed
                )
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
        }

        Spacer(Modifier.height(22.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardWhite)
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(6.dp))
                Text("Prefixos bloqueados", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text("Ex: 0800, 11, +55", fontSize = 13.sp, color = TextGray)
            Spacer(Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                OutlinedTextField(
                    value = state.prefixInput,
                    onValueChange = { viewModel.onEvent(RuleEvent.OnPrefixInputChanged(it)) },
                    placeholder = { Text("Digite um prefixo", color = TextGray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryRed,
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        cursorColor = PrimaryRed
                    )
                )

                Spacer(Modifier.width(12.dp))

                FilledTonalButton(
                    onClick = { viewModel.onEvent(RuleEvent.OnAddPrefix(state.prefixInput.trim())) },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = PrimaryRed
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "+",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Column {
                state.prefixList.forEach { prefix ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            prefix,
                            color = PrimaryRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        IconButton(
                            onClick = { viewModel.onEvent(RuleEvent.OnRemovePrefix(prefix)) }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardWhite)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Restrições de horário", fontWeight = FontWeight.SemiBold)
                RedSwitch(
                    checked = state.timeRestrictionEnabled,
                    onCheckedChange = { viewModel.onEvent(RuleEvent.OnToggleTimeRestriction(it)) }
                )
            }

            if (state.timeRestrictionEnabled) {
                Spacer(Modifier.height(16.dp))
                Row {
                    TimeSelector(
                        label = "Início",
                        time = state.startTime,
                        onSelect = { viewModel.onEvent(RuleEvent.OnStartTimeChanged(it)) }
                    )
                    Spacer(Modifier.width(12.dp))
                    TimeSelector(
                        label = "Fim",
                        time = state.endTime,
                        onSelect = { viewModel.onEvent(RuleEvent.OnEndTimeChanged(it)) }
                    )
                }
                Spacer(Modifier.height(20.dp))
                Text("Dias da semana", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(10.dp))

                Column {
                    DayOfWeek.entries.chunked(4).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            row.forEach { day ->
                                DayChip(
                                    label = day.toPtBrLabel(),
                                    selected = day in state.selectedDays,
                                    color = PrimaryRed
                                ) {
                                    viewModel.onEvent(RuleEvent.OnDaySelected(day))
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))


        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { viewModel.onEvent(RuleEvent.OnCancelClicked) },
                modifier = Modifier.weight(1f)
            ) { Text("Cancelar", color = Color.Red) }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = { viewModel.onEvent(RuleEvent.OnCreateClicked) },
                enabled = state.isFormValid,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                modifier = Modifier.weight(1f)
            ) {
                Text("Criar", color = Color.Black)
            }
        }
    }
}


@Composable
fun RedSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = PrimaryRed,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color(0xFFBDBDBD)
        )
    )
}

@Composable
fun TimeSelector(label: String, time: String, onSelect: (String) -> Unit) {
    val context = LocalContext.current
    Column {
        Text(label, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        OutlinedButton(
            onClick = {
                val cal = Calendar.getInstance()
                TimePickerDialog(
                    context,
                    { _, h, m -> onSelect(String.format("%02d:%02d", h, m)) },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            },
            modifier = Modifier.width(120.dp)
        ) {
            Text(time)
        }
    }
}

@Composable
fun DayChip(label: String, selected: Boolean, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (selected) color else Color(0xFFE0E0E0)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}
