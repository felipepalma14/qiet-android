package com.motoacademy.android.qiet.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    object BlockDashboardScreen: Screen

    @Serializable
    object HistoryScreen: Screen

    @Serializable
    object AddCategoryScreen: Screen

}
