package com.motoacademy.android.qiet.features.dashboard.presentation

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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.motoacademy.android.qiet.data.local.model.DayOfWeek
import com.motoacademy.android.qiet.data.local.model.toPtBrLabel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRuleScreen(
    navController: NavController,
    ruleId: Long
) {
    val context = LocalContext.current
    val viewModel: BlockDashboardViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var ruleData by remember { mutableStateOf<com.motoacademy.android.qiet.domain.model.BlockRule?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var ruleName by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(true) }
    var prefixInput by remember { mutableStateOf("") }
    var prefixList by remember { mutableStateOf<List<String>>(emptyList()) }
    var timeRestrictionEnabled by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("22:00") }
    var selectedDays by remember { mutableStateOf<List<DayOfWeek>>(emptyList()) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(ruleId) {
        println("📥 [EditScreen] Carregando regra ID: $ruleId")
        isLoading = true
        try {
            val fullRule = viewModel.getFullRuleById(ruleId)
            if (fullRule != null) {
                ruleData = fullRule

                ruleName = fullRule.ruleName
                isEnabled = fullRule.isEnabled
                prefixList = fullRule.blockedRegexRules
                timeRestrictionEnabled = fullRule.interval != null
                startTime = fullRule.interval?.startTime ?: "09:00"
                endTime = fullRule.interval?.endTime ?: "22:00"
                selectedDays = fullRule.interval?.daysOfWeek ?: emptyList()

                println("✅ [EditScreen] Dados carregados:")
                println("✅ [EditScreen]   Nome: '$ruleName'")
                println("✅ [EditScreen]   Status: $isEnabled")
                println("✅ [EditScreen]   Prefixos: $prefixList")
                println("✅ [EditScreen]   Intervalo: ${fullRule.interval?.let { "${it.startTime}-${it.endTime}" } ?: "null"}")
            } else {
                errorMessage = "Regra não encontrada"
                println("❌ [EditScreen] Regra $ruleId não encontrada")
            }
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar: ${e.message}"
            println("❌ [EditScreen] Erro ao carregar: ${e.message}")
            isLoading = false
        }
    }

    fun saveChanges() {
        println("\n[EditScreen] ===== INICIANDO SALVAMENTO =====")
        println("[EditScreen] ID da regra: $ruleId")
        println("[EditScreen] Novo nome: '$ruleName'")
        println(" [EditScreen] Status: $isEnabled")
        println("[EditScreen] Prefixos: $prefixList")
        println("[EditScreen] Restrição de tempo: $timeRestrictionEnabled")
        println("[EditScreen] Horário: $startTime - $endTime")
        println("[EditScreen] Dias selecionados: $selectedDays")

        isSaving = true

        coroutineScope.launch {
            try {
                // 🔥 CHAMA A FUNÇÃO DE UPDATE DO VIEWMODEL
                viewModel.updateRule(
                    ruleId = ruleId,
                    ruleName = ruleName,
                    isEnabled = isEnabled,
                    prefixList = prefixList,
                    timeRestrictionEnabled = timeRestrictionEnabled,
                    startTime = if (timeRestrictionEnabled) startTime else null,
                    endTime = if (timeRestrictionEnabled) endTime else null,
                    selectedDays = if (timeRestrictionEnabled) selectedDays else null
                )

                //  verifica se salvou
                kotlinx.coroutines.delay(1000)

                val savedSuccessfully = viewModel.verifyRuleWasSaved(ruleId, ruleName)

                if (savedSuccessfully) {
                    println(" [EditScreen] SALVAMENTO BEM-SUCEDIDO!")
                    Toast.makeText(context, "Regra atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                } else {
                    println("❌ [EditScreen] FALHA NA VERIFICAÇÃO!")
                    Toast.makeText(context, "Erro: Não foi possível verificar o salvamento", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                println("[EditScreen] ERRO ao salvar: ${e.message}")
                Toast.makeText(context, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
                isSaving = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Carregando regra...")
            }
        }
        return
    }

    if (errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Erro: $errorMessage", color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigateUp() }) {
                    Text("Voltar")
                }
            }
        }
        return
    }

    if (ruleData == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Regra não encontrada")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigateUp() }) {
                    Text("Voltar")
                }
            }
        }
        return
    }

    val screenScroll = rememberScrollState()

    val daysOfWeek = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Regra") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                }
            )
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(screenScroll)
        ) {
            Text(
                text = "Editar regra",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(16.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Nome da regra",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = ruleName,
                        onValueChange = { ruleName = it },
                        placeholder = { Text("Nome da regra") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(Modifier.height(16.dp))


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
                                    prefixList = prefixList + prefixInput.trim()
                                    prefixInput = ""
                                    println("➕ [EditScreen] Prefixo adicionado: $prefixList")
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
                            IconButton(
                                onClick = {
                                    prefixList = prefixList - prefix
                                    println("➖ [EditScreen] Prefixo removido: $prefix")
                                }
                            ) {
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
                            onCheckedChange = {
                                timeRestrictionEnabled = it
                                println("⏰ [EditScreen] Restrição de tempo: $it")
                            }
                        )
                    }

                    if (timeRestrictionEnabled) {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TimePickerButton("Início", startTime) {
                                startTime = it
                                println("[EditScreen] Start time: $it")
                            }
                            TimePickerButton("Fim", endTime) {
                                endTime = it
                                println("[EditScreen] End time: $it")
                            }
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
                                                    selectedDays = if (isSelected) {
                                                        selectedDays - day
                                                    } else {
                                                        selectedDays + day
                                                    }
                                                    println("📅 [EditScreen] Dias selecionados: $selectedDays")
                                                }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = day.toPtBrLabel(),
                                                color = if (isSelected) Color.White else Color.Black,
                                                fontSize = 14.sp,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        println("❌ [EditScreen] Cancelar ")
                        navController.navigateUp()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        println(" [EditScreen] Botão SALVAR clicado!")
                        saveChanges()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Salvando...")
                    } else {
                        Text("Salvar Alterações")
                    }
                }
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
                println("⏰ [EditScreen] Abrindo TimePicker para: $label")
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        val formattedTime =
                            String.format("%02d:%02d", selectedHour, selectedMinute)
                        println("⏰ [EditScreen] Time selecionado: $formattedTime")
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