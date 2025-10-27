package com.motoacademy.android.qiet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.motoacademy.android.qiet.features.call_history.presentation.CallHistoryScreen
import com.motoacademy.android.qiet.features.create_category.AddCategoryScreen
import com.motoacademy.android.qiet.features.dashboard.presentation.BlockDashboardScreen

@Composable
fun NavHostContainer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    //navegação

    NavHost(
        navController = navController,
        startDestination = Screen.BlockDashboardScreen,
        modifier = modifier,
        builder = {
            composable<Screen.BlockDashboardScreen> {
                BlockDashboardScreen()

            }

            composable<Screen.HistoryScreen> {
                CallHistoryScreen()
            }

            composable<Screen.AddCategoryScreen> {
                AddCategoryScreen(navController = navController)
            }
        }
    )
}