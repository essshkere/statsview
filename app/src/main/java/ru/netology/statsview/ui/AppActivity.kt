package ru.netology.statsview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.statsview.R


class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<StatsView>(R.id.stats).data = listOf(
            0.25F,
            0.25F,
            0.25F,
            0.25F,
        )
    }
}
