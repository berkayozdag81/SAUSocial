package com.berkayozdag.sausocial.ui.post_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentPostCreateBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PostCreateFragment : Fragment() {

    private var _binding: FragmentPostCreateBinding? = null
    private val binding get() = _binding!!
    private val postCreateViewModel: PostCreateViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
    private val currentDate = Date()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupListeners()
        setUpObserves()
    }

    private fun initViews() {
        binding.textViewUserName.text = sessionManager.getUserName()
    }

    private fun setupListeners() = with(binding) {
        editTextPostDescription.addTextChangedListener {
            it?.let {
                if (it.isNotEmpty()) {
                    buttonSend.isClickable = true
                    buttonSend.alpha = 1f
                } else {
                    buttonSend.isClickable = false
                    buttonSend.alpha = 0.5f
                }
            }
        }

        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonSend.setOnClickListener {
            val formattedDate = dateFormat.format(currentDate)

            postCreateViewModel.sendPost(
                editTextPostDescription.text.toString(),
                formattedDate,
                0,
                sessionManager.getUserId()
            )
        }
    }

    private fun setUpObserves() {
        postCreateViewModel.postCreateResponse.observe(viewLifecycleOwner) {response->
            when (response) {
                is NetworkResponse.Loading -> {

                }
                is NetworkResponse.Success -> {
                    context?.showToast("Post paylaşıldı")
                    findNavController().popBackStack()
                }
                is NetworkResponse.Error -> {
                    context?.showToast("Post paylaşılmadı")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}