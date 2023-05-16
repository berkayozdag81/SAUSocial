package com.berkayozdag.sausocial.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentLoginBinding
import com.berkayozdag.sausocial.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setupObserves()
        setupListeners()

    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            loginViewModel.login(
                binding.fragmentLoginTextInputUserName.text.toString(),
                binding.fragmentLoginTextInputPassword.text.toString()
            )
        }
    }

    private fun setupObserves() {
        loginViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                    //some code
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
                is NetworkResponse.Success -> {
                    //navigate to home
                    binding.loginProgressBar.visibility = View.GONE
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                is NetworkResponse.Error -> {
                    //error
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(this.context,"Hatalı kullanıcı adı veya şifre", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}