package com.github.inlinefun.lazygreetings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.navigation.LazyNavigationHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyGreetingsTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyNavigationHost()
                }
            }
        }
    }
}
