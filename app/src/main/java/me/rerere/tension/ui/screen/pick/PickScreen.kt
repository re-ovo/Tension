package me.rerere.tension.ui.screen.pick

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.tension.ui.components.BackIcon
import me.rerere.tension.ui.components.Md3TopBar

@Destination
@Composable
fun PickScreen(
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text(text = "添加数据源")
                },
                navigationIcon = {
                    BackIcon(navigator)
                }
            )
        }
    ) {
        
    }
}