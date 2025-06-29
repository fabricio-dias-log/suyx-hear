package com.example.suyxhear

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.suyxhear.databinding.FragmentSettingsBinding
import com.example.suyxhear.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSettings()

        binding.buttonLogout.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }

        binding.buttonSave.setOnClickListener {
            saveAllSettings()
            Snackbar.make(view, "Alterações salvas com sucesso!", Snackbar.LENGTH_SHORT).show()
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        binding.sliderDbLimit.addOnChangeListener { _, value, _ ->
            binding.textDbLimitValue.text = "${value.toInt()} dB"
        }
    }

    private fun saveAllSettings() {
        val sharedPref = activity?.getSharedPreferences("SuyxHearPrefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            // Salvar nome
            putString("USER_NAME", binding.editTextName.text.toString())
            // Salvar tema
            putBoolean("NIGHT_MODE", binding.switchTheme.isChecked)
            // Salvar limite de dB
            val newLimit = binding.sliderDbLimit.value.toInt()
            putInt("DB_LIMIT", newLimit)
            sharedViewModel.setDbLimit(newLimit)

            apply()
        }
    }

    private fun loadSettings() {
        val sharedPref = activity?.getSharedPreferences("SuyxHearPrefs", Context.MODE_PRIVATE) ?: return
        val name = sharedPref.getString("USER_NAME", "")
        val isNightMode = sharedPref.getBoolean("NIGHT_MODE", false)
        val dbLimit = sharedPref.getInt("DB_LIMIT", 55)

        binding.editTextName.setText(name)
        binding.switchTheme.isChecked = isNightMode
        binding.sliderDbLimit.value = dbLimit.toFloat()
        binding.textDbLimitValue.text = "$dbLimit dB"
        sharedViewModel.setDbLimit(dbLimit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}