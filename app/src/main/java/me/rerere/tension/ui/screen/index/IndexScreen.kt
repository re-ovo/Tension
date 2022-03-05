package me.rerere.tension.ui.screen.index

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.tension.provider.DataProvider
import me.rerere.tension.provider.game.EpicGameSource
import me.rerere.tension.ui.components.Md3TopBar
import me.rerere.tension.ui.screen.destinations.PickScreenDestination
import org.koin.androidx.compose.getViewModel

@Destination(start = true)
@Composable
fun IndexScreen(
    navigator: DestinationsNavigator,
    indexViewModel: IndexViewModel = getViewModel()
) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text(text = "Tension")
                },
                actions = {
                    IconButton(onClick = {
                        navigator.navigate(PickScreenDestination)
                    }) {
                        Icon(Icons.Rounded.Add, null)
                    }
                }
            )
        }
    ) {
        val epicSource = remember { EpicGameSource() }
        PreviewCard(epicSource)
    }
}

@Composable
fun PreviewCard(provider: DataProvider) {
    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = provider.getDisplayName(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            provider.getPreviewUI().invoke()
        }
    }
}