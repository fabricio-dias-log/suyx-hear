package com.example.suyxhear

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.suyxhear.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignup.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                saveUserName(name)
                Snackbar.make(view, "Registro realizado com sucesso!", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signUpFragment_to_monitorFragment)
            } else {
                Snackbar.make(view, "Por favor, preencha todos os campos.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserName(name: String) {
        val sharedPref = activity?.getSharedPreferences("SuyxHearPrefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("USER_NAME", name)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
