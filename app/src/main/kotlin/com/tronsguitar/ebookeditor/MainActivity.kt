package com.tronsguitar.ebookeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tronsguitar.ebookeditor.ui.navigation.AppNavigation
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The single entry-point Activity for The Digital Study.
 *
 * Edge-to-edge rendering is enabled so the app can draw behind system bars,
 * which is important for the full-bleed editor surface on tablets and foldables.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EbookEditorTheme {
                AppNavigation()
            }
        }
    }
}
