package com.motoacademy.android.qiet.ui.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class BlockRule(
    val id: Long,
    val title: String,
    val isChecked: Boolean = false,
    val blockInterval: String? = null,
    val prefixLabel: String? = null,
    val blockedContactLabel: String? = null,
    val blockWeek: String? = null,
)

@Composable
fun BlockRuleInfo(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    color: Color,
    subtitle: String? = null,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = icon,
            contentDescription = text,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = text,
                fontWeight = fontWeight,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    fontWeight = FontWeight.Normal,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BlockRuleItem(
    modifier: Modifier = Modifier,
    data: BlockRule,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BlockRuleInfo(
                    text = data.title,
                    icon = Icons.Filled.Check,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                data.blockInterval?.let { blockInterval ->
                    BlockRuleInfo(
                        text = blockInterval,
                        subtitle = data.blockWeek,
                        icon = Icons.Filled.DateRange,
                        color = Color.Gray
                    )
                }

                data.prefixLabel?.let { prefixLabel ->
                    BlockRuleInfo(
                        text = prefixLabel,
                        icon = Icons.Filled.Call,
                        color = Color.Gray
                    )
                }

                data.blockedContactLabel?.let { blockedContactLabel ->
                    BlockRuleInfo(
                        text = blockedContactLabel,
                        color = Color.Gray,
                        icon = Icons.Filled.Face
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
            ) {
                Switch(
                    checked = data.isChecked,
                    onCheckedChange = {
                        onCheckedChange(it)
                    },
                    thumbContent = {
                        Icon(
                            imageVector = if (data.isChecked) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedIconColor = Color.White,
                        checkedBorderColor = Color.Black,
                        checkedTrackColor = Color.Black,
                        uncheckedThumbColor = Color.White,
                        uncheckedIconColor = Color.LightGray,
                        uncheckedTrackColor = Color.LightGray,
                        disabledCheckedThumbColor = Color.Green.copy(alpha = 0.3f),
                        disabledUncheckedThumbColor = Color.Red.copy(alpha =  0.3f),
                    )
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun BlockRuleItemPreview() {
    val item = BlockRule(
        id = 1L,
        title = "Regra de Bloqueio",
        prefixLabel = "Prefixo(s): 3030, 4040",
        blockInterval = "Bloqueio: 08:00 - 18:00",
        blockWeek = "Final de semana",
        blockedContactLabel = "2 Contato(s) bloqueado(s)",
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        BlockRuleItem(
            modifier = Modifier.fillMaxWidth(),
            data = item,
            onClick = {

            },
            onCheckedChange = {

            }
        )
    }
}