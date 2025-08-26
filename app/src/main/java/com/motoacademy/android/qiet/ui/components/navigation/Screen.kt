package com.motoacademy.android.qiet.ui.components.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    object BlockDashboardScreen: Screen

    @Serializable
    object HistoryScreen: Screen

}
