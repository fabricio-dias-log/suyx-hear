package com.example.suyxhear

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SuyxHearApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Carrega a preferência do tema na inicialização do app
        val sharedPref = getSharedPreferences("SuyxHearPrefs", Context.MODE_PRIVATE)
        val isNightMode = sharedPref.getBoolean("NIGHT_MODE", false)
        val mode = if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}