package com.example.suyxhear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.suyxhear.data.NoiseRecord

class SharedViewModel : ViewModel() {

    // Limite de Decibéis
    private val _dbLimit = MutableLiveData<Int>(55)
    val dbLimit: LiveData<Int> = _dbLimit

    fun setDbLimit(limit: Int) {
        _dbLimit.value = limit
    }

    // Histórico de Ruído
    private val _noiseHistory = MutableLiveData<MutableList<NoiseRecord>>(mutableListOf())
    val noiseHistory: LiveData<MutableList<NoiseRecord>> = _noiseHistory

    fun addNoiseRecord(record: NoiseRecord) {
        val list = _noiseHistory.value ?: mutableListOf()
        list.add(record)
        // Mantém apenas os últimos 300 registros para não sobrecarregar a memória
        if (list.size > 300) {
            list.removeAt(0)
        }
        _noiseHistory.postValue(list)
    }
}
