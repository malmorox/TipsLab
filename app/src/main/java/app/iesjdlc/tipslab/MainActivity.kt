package app.iesjdlc.tipslab

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import app.iesjdlc.tipslab.navigation.AppNavigation
import app.iesjdlc.tipslab.ui.theme.TipsLabTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TipsLabTheme {
                AppNavigation()
            }
        }
    }
}