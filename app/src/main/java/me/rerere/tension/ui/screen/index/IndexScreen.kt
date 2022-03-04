package me.rerere.tension.ui.screen.index

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.tension.ui.components.Md3TopBar
import org.koin.androidx.compose.getViewModel

@Destination(start = true)
@Composable
fun IndexScreen(
    destinationsNavigator: DestinationsNavigator,
    indexViewModel: IndexViewModel = getViewModel()
) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text(text = "Index")
                }
            )
        }
    ) {

    }
}