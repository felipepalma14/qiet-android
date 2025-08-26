package com.motoacademy.android.qiet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.motoacademy.android.qiet.ui.components.navigation.BottomNavigationBar
import com.motoacademy.android.qiet.ui.components.navigation.NavHostContainer
import com.motoacademy.android.qiet.ui.theme.QietTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QietTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        bottomBar = { BottomNavigationBar(navController = navController) }
                    ) { innerPadding ->
                        NavHostContainer(
                            modifier = Modifier
                                .padding(innerPadding),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
