package com.motoacademy.android.qiet.utils

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.widget.Toast

object DialerUtils {

    fun requestDefaultDialer(activity: Activity, launcher: (Intent) -> Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // Android 10+ usa RoleManager
//            val roleManager = activity.getSystemService(RoleManager::class.java)
//            if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
//                if (roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
//                    Toast.makeText(activity, "Este app já é o discador padrão", Toast.LENGTH_SHORT).show()
//                } else {
//                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
//                    launcher(intent)
//                }
//            }
//        } else {
            // Android 9 ou inferior usa Intent clássica
            val telecomManager = activity.getSystemService(TelecomManager::class.java)
            val current = telecomManager.defaultDialerPackage
            if (current == activity.packageName) {
                Toast.makeText(activity, "Este app já é o discador padrão", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                    putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, activity.packageName)
                }
                launcher(intent)
            }
//        }
    }

    fun isDefaultDialer(context: Context): Boolean {
        val telecomManager = context.getSystemService(TelecomManager::class.java)
        return context.packageName == telecomManager.defaultDialerPackage
    }
}