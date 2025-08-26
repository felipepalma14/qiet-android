package com.motoacademy.android.qiet.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: Screen,
    val icon: ImageVector,
    val title: String,
    val contentDescription: String? = null
) {
    object BlockDashboardScreen : BottomNavItem(
        route = Screen.BlockDashboardScreen,
        icon = Icons.Default.Lock,
        title = "Bloqueios"
    )

    object HistoryScreen : BottomNavItem(
        route = Screen.HistoryScreen,
        icon = Icons.Default.Menu,
        title ="Histórico"
    )
}
