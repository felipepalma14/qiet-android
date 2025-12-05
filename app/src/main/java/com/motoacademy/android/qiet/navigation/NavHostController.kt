package com.motoacademy.android.qiet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.motoacademy.android.qiet.features.call_history.presentation.CallHistoryScreen
import com.motoacademy.android.qiet.features.create_category.AddCategoryScreen
import com.motoacademy.android.qiet.features.dashboard.presentation.BlockDashboardScreen
import com.motoacademy.android.qiet.features.dashboard.presentation.EditRuleScreen


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

            composable<Screen.AddCategoryScreen> {
                AddCategoryScreen(navController = navController)
            }


            composable(
                route = "edit_rule/{ruleId}",
                arguments = listOf(navArgument("ruleId") { type = NavType.LongType })
            ) { backStackEntry ->
                val ruleId = backStackEntry.arguments?.getLong("ruleId") ?: 0L
                EditRuleScreen(
                    navController = navController,
                    ruleId = ruleId
                )
            }
        }
    )
}