package com.motoacademy.android.qiet.ui.components.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.ui.components.list.SpamCall
import com.motoacademy.android.qiet.ui.components.list.SpamCallItem

@Immutable
data class SearchFilterBarModel(
    val filterButtonTitle: String,
    val searchPlaceholder: String,
    val dialogTitle: String,
    val categoryList: List<BlockRuleEntity>
)

@Composable
fun SearchFilterBar(
    modifier: Modifier = Modifier,
    data: SearchFilterBarModel,
    onSearch: (String) -> Unit,
    onFilterSelected: (BlockRuleEntity) -> Unit,
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                Color.White,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Campo de busca
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                onSearch(it.text) // retorna o valor digitado p/ ViewModel
            },
            placeholder = { Text(data.searchPlaceholder) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
            },
            modifier = Modifier
                .weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Botão de filtro
        Button(
            onClick = { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Filter",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(data.filterButtonTitle, color = Color.Black)
        }
    }

    // Dialog de opções de filtro
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            title = { Text(data.dialogTitle) },
            text = {
                LazyColumn {
                    items(
                        items = data.categoryList,
                        key = { it.id }
                    ) { item ->
                        FilterOption(item.ruleName) {
                            onFilterSelected(item)
                            onSearch(item.ruleName)
                            textState = TextFieldValue(item.ruleName)
                            showDialog = false
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun FilterOption(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarWithFilterPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        SearchFilterBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(8.dp),
            data = SearchFilterBarModel(
                searchPlaceholder = "Buscar por número",
                filterButtonTitle = "Filter",
                dialogTitle = "Selecione a categoria",
                categoryList = listOf()
            ),
            onSearch = {},
            onFilterSelected = {}
        )
    }

}