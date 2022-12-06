package com.copperleaf.thistle.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.copperleaf.thistle.android.example.databinding.MainActivityBinding
import com.copperleaf.thistle.android.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            MainActivityBinding
                .inflate(layoutInflater)
                .apply {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, MainFragment())
                        .commit()
                }
                .root
        )
    }
}
