package com.github.inlinefun.lazygreetings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.navigation.LazyNavigationHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyGreetingsTheme {
                LazyNavigationHost()
            }
        }
    }
}
