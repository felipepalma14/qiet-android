package com.motoacademy.android.qiet.services

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import android.telecom.VideoProfile
import com.motoacademy.android.qiet.features.income_call.IncomingCallActivity

class QietInCallService : InCallService() {

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        // Abre sua tela personalizada ao receber ligação
        val intent = Intent(this, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("call_id", call.details.handle.schemeSpecificPart)
        }
        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
    }

    companion object {
        private var currentCall: Call? = null

        fun setCall(call: Call) {
            currentCall = call
        }

        fun answer() {
            currentCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
        }

        fun hangup() {
            currentCall?.disconnect()
        }
    }
}
