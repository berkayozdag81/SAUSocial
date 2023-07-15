package com.berkayozdag.sausocial.ui.post_create

import android.animation.Animator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.common.util.setVisible
import com.berkayozdag.sausocial.common.util.showToast
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
    @RequiresApi(Build.VERSION_CODES.N)
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT))
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupListeners()
        setupObserves()
    }

    private fun initViews() = with(binding) {
        textViewUserName.text = sessionManager.getUserName()
        imageViewUserProfile.loadImage(sessionManager.getUserProfileImage())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupListeners() = with(binding) {
        editTextPostDescription.addTextChangedListener { text ->
            if (text?.isNotEmpty() == true) {
                buttonSend.isEnabled = true
                buttonSend.alpha = 1f
            } else {
                buttonSend.isEnabled = false
                buttonSend.alpha = 0.5f
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

        sharePostSuccessAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                // onAnimationStart logic
            }

            override fun onAnimationEnd(p0: Animator) {
                findNavController().popBackStack()
            }

            override fun onAnimationCancel(p0: Animator) {
                // onAnimationCancel logic
            }

            override fun onAnimationRepeat(p0: Animator) {
                // onAnimationRepeat logic
            }
        })

    }

    private fun setupObserves() {
        postCreateViewModel.postCreateResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    with(binding) {
                        sharePostSuccessAnimation.setVisible(true)
                        sharePostSuccessAnimation.playAnimation()
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Post paylaşılamadı.")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}