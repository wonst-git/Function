package com.example.fingerprintex

import androidx.biometric.BiometricManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import com.example.fingerprintex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBio.isEnabled = getBioAvailable()
        binding.btnBio.setOnClickListener {
            setBiometricPrompt().authenticate(getPromptInfo())
        }
    }

    private fun setBiometricPrompt() = BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            binding.txtBio.text = "인증 성공!"
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            binding.txtBio.text = errString
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            binding.txtBio.text = "인증 실패!"
        }
    })

    private fun getPromptInfo() = BiometricPrompt.PromptInfo.Builder().apply {
        setTitle("생체정보를 인증해주세요.")
        setNegativeButtonText("취소")
    }.build()

    private fun getBioAvailable() : Boolean {
        BiometricManager.from(this).also {
            return when(it.canAuthenticate(BIOMETRIC_WEAK)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                   true
                }
                else -> {
                    false
                }
            }
        }
    }
}