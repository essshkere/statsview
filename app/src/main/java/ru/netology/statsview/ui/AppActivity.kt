package ru.netology.statsview.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import ru.netology.statsview.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statsView = findViewById<StatsView>(R.id.stats)

        findViewById<StatsView>(R.id.stats).data = listOf(
            0.25F,
            0.25F,
            0.25F,
            0.25F,
        )


        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                statsView.setRotationProgress(animation.animatedValue as Float)
            }
        }
        animator.start()

    }}