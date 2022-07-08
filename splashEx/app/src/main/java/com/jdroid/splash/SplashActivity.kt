package com.jdroid.splash

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.window.SplashScreen
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val animScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 8F)
                val animScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 8f)
                val animAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)

                ObjectAnimator.ofPropertyValuesHolder(splashScreenView.iconView, animAlpha, animScaleX, animScaleY).run {
                    interpolator = AnticipateInterpolator()
                    duration = 300L
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }

//        val content = findViewById<View>(android.R.id.content)
//        content.viewTreeObserver.addOnPreDrawListener (ViewTreeObserver.OnPreDrawListener {
//            if(splashScreen.isReady) {
//                content.viewTreeObserver.removeOnPreDrawListener(this)
//                true
//            } else {
//                false
//            }
//        })
    }
}