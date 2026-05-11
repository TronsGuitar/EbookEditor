package com.thedigitalstudy.ebookeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.thedigitalstudy.ebookeditor.navigation.AppNavigation
import com.thedigitalstudy.ebookeditor.ui.theme.TheDigitalStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheDigitalStudyTheme {
                AppNavigation()
            }
        }
    }
}
