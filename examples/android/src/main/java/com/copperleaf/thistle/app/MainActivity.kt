package com.copperleaf.thistle.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.copperleaf.thistle.app.databinding.MainActivityBinding
import com.copperleaf.thistle.app.ui.main.MainFragment

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
