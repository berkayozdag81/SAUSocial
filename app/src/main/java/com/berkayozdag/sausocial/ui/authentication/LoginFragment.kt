package com.berkayozdag.sausocial.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkayozdag.sausocial.databinding.FragmentLoginBinding
import com.berkayozdag.sausocial.ui.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }
    private fun setupListeners(){
        binding.loginButton.setOnClickListener {
            login()
        }
    }
    private fun login(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}