package com.berkayozdag.sausocial.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.berkayozdag.sausocial.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}