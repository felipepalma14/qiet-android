package com.motoacademy.android.qiet.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.motoacademy.android.qiet.features.call_history.presentation.CallHistoryScreen
import com.motoacademy.android.qiet.features.dashboard.presentation.BlockDashboardScreen

@Composable
fun NavHostContainer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.BlockDashboardScreen,
        modifier = modifier,
        builder = {
            composable<Screen.BlockDashboardScreen> {
                BlockDashboardScreen(navController = navController)
            }

            composable<Screen.HistoryScreen> {
                CallHistoryScreen()
            }
        }
    )
}