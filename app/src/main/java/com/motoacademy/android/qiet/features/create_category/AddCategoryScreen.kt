package com.motoacademy.android.qiet.features.create_category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.motoacademy.android.qiet.ui.components.card.AddRuleNameCard
import com.motoacademy.android.qiet.ui.components.card.BlockerRuleCard
import com.motoacademy.android.qiet.ui.components.card.SelectRuleColorCard
import com.motoacademy.android.qiet.ui.theme.BlueCategory

@Composable
fun AddCategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val screenScroll = rememberScrollState()
    var ruleName by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf(BlueCategory) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(screenScroll)
    ) {
        Text(
            modifier = Modifier,
            text = "Nova regra",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 18.sp
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
        SelectRuleColorCard(title = "Cor da regra", color = color, onColorSelected = {
            color = it
        })

        Spacer(Modifier.height(16.dp))

        BlockerRuleCard(
            title = "Regras de bloqueio",
            onItemAdded = {
            }
        )
    }
}
