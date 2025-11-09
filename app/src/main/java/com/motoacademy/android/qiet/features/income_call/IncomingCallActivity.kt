package com.motoacademy.android.qiet.features.income_call

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.motoacademy.android.qiet.services.QietInCallService

class IncomingCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val phoneNumber = intent.getStringExtra("call_id") ?: "Número desconhecido"
        setContent {
            IncomingCallScreen(
                number = phoneNumber,
                onAccept = {
                    QietInCallService.answer()
                    finish()
                },
                onReject = {
                    QietInCallService.hangup()
                    finish()
                }
            )
        }
    }
}
