package com.berkayozdag.sausocial.ui.post_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentPostCreateBinding
import com.berkayozdag.sausocial.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PostCreateFragment : Fragment() {

    private var _binding: FragmentPostCreateBinding? = null
    private val binding get() = _binding!!
    private val postCreateViewModel: PostCreateViewModel by viewModels()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val currentDate = Date()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostCreateBinding.inflate(inflater, container, false)

        setupListeners()
        setUpObserves()
        return binding.root
    }

    private fun initView(){

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
        postCreateViewModel.postCreateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {

                }
                is NetworkResponse.Success -> {
                    Toast.makeText(this.context, "Post paylaşıldı", Toast.LENGTH_LONG).show()
                }
                is NetworkResponse.Error -> {
                    Toast.makeText(this.context, "Post paylaşılmadı", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}