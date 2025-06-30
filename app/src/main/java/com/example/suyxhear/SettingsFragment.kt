package com.example.suyxhear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.suyxhear.data.DataRepository
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
        // Salva através do ViewModel, que por sua vez chama o Repository
        sharedViewModel.setUserName(binding.editTextName.text.toString())
        sharedViewModel.setDbLimit(binding.sliderDbLimit.value.toInt())

        // Salva o tema diretamente, pois afeta a UI de imediato
        val repository = DataRepository(requireContext())
        repository.saveBoolean(DataRepository.KEY_NIGHT_MODE, binding.switchTheme.isChecked)
    }

    private fun loadSettings() {
        val repository = DataRepository(requireContext())

        sharedViewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.editTextName.setText(name)
        }
        sharedViewModel.dbLimit.observe(viewLifecycleOwner) { limit ->
            binding.sliderDbLimit.value = limit.toFloat()
            binding.textDbLimitValue.text = "$limit dB"
        }
        binding.switchTheme.isChecked = repository.getBoolean(DataRepository.KEY_NIGHT_MODE, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
