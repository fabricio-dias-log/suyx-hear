package com.example.suyxhear.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.suyxhear.data.DataRepository
import com.example.suyxhear.data.NoiseRecord

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataRepository(application)

    // --- User Settings ---
    private val _dbLimit = MutableLiveData<Int>()
    val dbLimit: LiveData<Int> = _dbLimit

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    // --- Noise History ---
    private val _noiseHistory = MutableLiveData<MutableList<NoiseRecord>>()
    val noiseHistory: LiveData<MutableList<NoiseRecord>> = _noiseHistory

    init {
        // Carrega dados persistidos quando o ViewModel é criado
        _dbLimit.value = repository.getInt(DataRepository.KEY_DB_LIMIT, 55)
        _userName.value = repository.getString(DataRepository.KEY_USER_NAME, "Usuário")
        _noiseHistory.value = repository.loadHistory()
    }

    fun setDbLimit(limit: Int) {
        _dbLimit.value = limit
        repository.saveInt(DataRepository.KEY_DB_LIMIT, limit)
    }

    fun setUserName(name: String) {
        _userName.value = name
        repository.saveString(DataRepository.KEY_USER_NAME, name)
    }

    fun addNoiseRecord(record: NoiseRecord) {
        val list = _noiseHistory.value ?: mutableListOf()
        list.add(record)
        if (list.size > 300) { // Limita o tamanho do histórico
            list.removeAt(0)
        }
        _noiseHistory.postValue(list) // Usa postValue para ser seguro em threads
        repository.saveHistory(list)
    }
}