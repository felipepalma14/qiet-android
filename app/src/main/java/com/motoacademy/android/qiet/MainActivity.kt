package com.motoacademy.android.qiet

import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.motoacademy.android.qiet.navigation.BottomNavigationBar
import com.motoacademy.android.qiet.navigation.NavHostContainer
import com.motoacademy.android.qiet.navigation.Screen
import com.motoacademy.android.qiet.ui.theme.QietTheme
import com.motoacademy.android.qiet.utils.DialerUtils
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dialerPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "App definido como discador padrão!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permissão negada. Algumas funções podem não funcionar.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Solicita o papel de discador padrão se ainda não for
        if (!DialerUtils.isDefaultDialer(this)) {
            DialerUtils.requestDefaultDialer(this) { intent ->
                dialerPermissionLauncher.launch(intent)
            }
        }

        setContent {
            QietTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination?.route

                var showDialog by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if (!DialerUtils.isDefaultDialer(this@MainActivity)) {
                        showDialog = true
                    }
                }

                if (showDialog) {
                    DefaultDialerPermissionDialog(
                        onConfirm = {
                            showDialog = false
                            DialerUtils.requestDefaultDialer(this@MainActivity) { intent ->
                                dialerPermissionLauncher.launch(intent)
                            }
                        },
                        onDismiss = {
                            showDialog = false
                            Toast.makeText(
                                this@MainActivity,
                                "Você pode conceder a permissão mais tarde nas configurações.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Scaffold(
                        floatingActionButton = {

                            if (currentDestination?.contains("BlockDashboardScreen") == true) {
                                FloatingActionButton(onClick = {
                                    navController.navigate(Screen.AddCategoryScreen)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "add"
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) { innerPadding ->
                        NavHostContainer(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultDialerPermissionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Definir como discador padrão") },
        text = {
            Text(
                "Para bloquear e gerenciar chamadas, este aplicativo precisa ser definido como discador padrão do sistema.\n\n" +
                        "Deseja permitir agora?",
                textAlign = TextAlign.Justify
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Permitir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
