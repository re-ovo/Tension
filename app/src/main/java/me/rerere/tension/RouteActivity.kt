package me.rerere.tension

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import me.rerere.tension.ui.screen.NavGraphs
import me.rerere.tension.ui.theme.TensionTheme
import me.rerere.tension.ui.transition.DefaultScreenTransition

class RouteActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the window to be fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TensionTheme {
                DestinationsNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navGraph = NavGraphs.root,
                    engine = rememberAnimatedNavHostEngine(
                        rootDefaultAnimations = DefaultScreenTransition
                    )
                )
            }
        }
    }
}
