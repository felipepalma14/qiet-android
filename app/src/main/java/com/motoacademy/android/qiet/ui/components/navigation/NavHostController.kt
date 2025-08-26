package com.motoacademy.android.qiet.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
//                HomeScreen(navController, snackbarHostState) {
//                    navController.navigate(Screen.DetailsScreen(item = it))
//                }
            }

            composable<Screen.HistoryScreen> {
//                FavoritesScreen(navController){
//                    navController.navigate(Screen.DetailsScreen(item = it))
//                }
            }
        }
    )
}