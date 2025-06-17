package ru.netology.statsview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.statsview.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapsFragment())
                .commit()
        }
    }

    fun showMarkersList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MarkersListFragment())
            .addToBackStack(null)
            .commit()
    }


}