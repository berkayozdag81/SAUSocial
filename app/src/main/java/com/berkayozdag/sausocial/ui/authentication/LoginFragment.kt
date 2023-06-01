package com.berkayozdag.sausocial.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.checkEditTexts
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentLoginBinding
import com.berkayozdag.sausocial.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObserves()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val username = binding.fragmentLoginTextInputUserName
            val password = binding.fragmentLoginTextInputPassword
            if (checkEditTexts(username, password)) {
                loginViewModel.login(
                    username.text.toString(),
                    password.text.toString()
                )
            }
        }
    }

    private fun setupObserves() {
        loginViewModel.loginState.observe(viewLifecycleOwner) {response->
            when (response) {
                is NetworkResponse.Loading -> {
                    binding.loginProgressBar.setVisible(true)
                }
                is NetworkResponse.Success -> {
                    binding.loginProgressBar.setVisible(false)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    sessionManager.setLogin(true)
                    sessionManager.setUserId(response.data.id)
                    sessionManager.setUserName(response.data.name + " " + response.data.surname)
                    activity?.finish()
                }
                is NetworkResponse.Error -> {
                    binding.loginProgressBar.setVisible(false)
                    context?.showToast("Hatalı kullanıcı adı veya şifre")
                }
            }
        }
    }
}