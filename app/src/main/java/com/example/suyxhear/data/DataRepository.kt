package com.example.suyxhear.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataRepository(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SuyxHearPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val KEY_USER_NAME = "USER_NAME"
        const val KEY_NIGHT_MODE = "NIGHT_MODE"
        const val KEY_DB_LIMIT = "DB_LIMIT"
        const val KEY_NOISE_HISTORY = "NOISE_HISTORY"
    }

    // --- User Settings ---
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // --- Noise History ---
    fun saveHistory(history: List<NoiseRecord>) {
        val jsonString = gson.toJson(history)
        saveString(KEY_NOISE_HISTORY, jsonString)
    }

    fun loadHistory(): MutableList<NoiseRecord> {
        val jsonString = getString(KEY_NOISE_HISTORY, "")
        if (jsonString.isBlank()) {
            return mutableListOf()
        }
        val type = object : TypeToken<MutableList<NoiseRecord>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}