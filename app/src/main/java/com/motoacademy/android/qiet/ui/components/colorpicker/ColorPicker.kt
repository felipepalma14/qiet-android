    package com.motoacademy.android.qiet.ui.components.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motoacademy.android.qiet.ui.theme.BlueCategory
import com.motoacademy.android.qiet.ui.theme.CyanCategory
import com.motoacademy.android.qiet.ui.theme.DeepOrangeCategory
import com.motoacademy.android.qiet.ui.theme.GreenCategory
import com.motoacademy.android.qiet.ui.theme.OrangeCategory
import com.motoacademy.android.qiet.ui.theme.PurpleCategory
import com.motoacademy.android.qiet.ui.theme.RedCategory
import com.motoacademy.android.qiet.ui.theme.TealCategory

private val CategoryColors = listOf(
    BlueCategory,
    TealCategory,
    OrangeCategory,
    RedCategory,
    PurpleCategory,
    CyanCategory,
    GreenCategory,
    DeepOrangeCategory
)

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        CategoryColors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (color == selectedColor) 3.dp else 1.dp,
                        color = if (color == selectedColor) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    var selectedColor by remember { mutableStateOf(CategoryColors.first()) }

    MaterialTheme {
        ColorPicker(
            selectedColor = selectedColor,
            onColorSelected = { selectedColor = it }
        )
    }
}