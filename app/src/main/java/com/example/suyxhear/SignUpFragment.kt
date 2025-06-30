package com.example.suyxhear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.suyxhear.data.DataRepository
import com.example.suyxhear.databinding.FragmentSignUpBinding
import com.example.suyxhear.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
                sharedViewModel.setUserName(name)

                val repository = DataRepository(requireContext())
                repository.saveBoolean(DataRepository.KEY_IS_LOGGED_IN, true)

                Snackbar.make(view, "Registro realizado com sucesso!", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signUpFragment_to_monitorFragment)
            } else {
                Snackbar.make(view, "Por favor, preencha todos os campos.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}