package com.jdroid.kakaoLoginEx

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.kakaoLoginEx.databinding.ActivityMainBinding
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(binding.root)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(Constants.TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(Constants.TAG, "사용자 정보 요청 성공 : $user")
                binding.txtNickName.text = user.kakaoAccount?.profile?.nickname
                binding.txtAge.text = user.kakaoAccount?.ageRange.toString()
                binding.txtEmail.text = user.kakaoAccount?.email
            }
        }
    }
}