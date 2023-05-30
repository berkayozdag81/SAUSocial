package com.berkayozdag.sausocial.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            splashLayout.animate().setDuration(1500).alpha(1f).withEndAction {
                val intent = if (!sessionManager.isLogin()) {
                    Intent(this@SplashActivity, AuthenticationActivity::class.java)
                } else {
                    Intent(this@SplashActivity, MainActivity::class.java)
                }
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}