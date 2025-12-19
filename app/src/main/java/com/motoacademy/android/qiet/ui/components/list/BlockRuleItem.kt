package com.motoacademy.android.qiet.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

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
fun BlockRuleItem(
    modifier: Modifier = Modifier,
    data: BlockRule,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    showActions: Boolean = false,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {

            // Cabeçalho: título + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = data.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))



                }

                Switch(
                    checked = data.isChecked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFD32F2F),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD8D8D8)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Informações integradas ao card, sem fundo cinza
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                data.blockInterval?.let { InfoRow("HORÁRIO", it, Color(0xFFAAAAAA), Color(0xFFD32F2F)) }
                data.blockWeek?.let { InfoRow("DIAS", it, Color(0xFFAAAAAA), Color(0xFF333333)) }
                data.prefixLabel?.let { InfoRow("PREFIXOS", it, Color(0xFFAAAAAA), Color(0xFF333333)) }
                data.blockedContactLabel?.let { InfoRow("CONTATOS", it, Color(0xFFAAAAAA), Color(0xFF333333)) }
            }

            // Botões de ação delicados
            if (showActions && (onEditClick != null || onDeleteClick != null)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    onEditClick?.let { DelicateActionButton(Icons.Outlined.Edit, "Editar", Color(
                        0xFF439DE7
                    ), it) }
                    Spacer(modifier = Modifier.width(8.dp))
                    onDeleteClick?.let { DelicateActionButton(Icons.Outlined.Delete, "Excluir", Color(
                        0xFFEF5555
                    ), it) }
                }
            }
        }
    }
}

@Composable
fun DelicateActionButton(icon: ImageVector, contentDescription: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.15f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = contentDescription, tint = color, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun InfoRow(label: String, value: String, labelColor: Color, valueColor: Color) {
    Column {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = labelColor)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = valueColor)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBlockRuleItem() {
    val item = BlockRule(
        id = 1L,
        title = "Bloqueio de Chamadas Comerciais",
        isChecked = true,
        blockInterval = "08:00 - 18:00",
        blockWeek = "Segunda a Sexta-feira",
        prefixLabel = "0303, 0404, 0505",
        blockedContactLabel = "3 contatos bloqueados"
    )

    BlockRuleItem(
        modifier = Modifier.fillMaxWidth(),
        data = item,
        onClick = {},
        onCheckedChange = {},
        showActions = true,
        onEditClick = {},
        onDeleteClick = {}
    )
}
