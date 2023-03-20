package com.berkayozdag.sausocial.ui.post_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.databinding.FragmentPostCreateBinding

class PostCreateFragment : Fragment() {

    private var _binding: FragmentPostCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostCreateBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners()= with(binding){
        editTextPostDescription.addTextChangedListener {
            it?.let {
                if(it.isNotEmpty()){
                    buttonSend.isClickable=true
                    buttonSend.alpha= 1f
                }
                else{
                    buttonSend.isClickable=false
                    buttonSend.alpha= 0.5f
                }
            }
        }
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}