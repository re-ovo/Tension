package me.rerere.tension.ui.screen.index

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.tension.provider.SubscribedDataProviderKey
import me.rerere.tension.provider.collectSubscribedDataProviders
import me.rerere.tension.ui.components.Md3TopBar
import me.rerere.tension.ui.components.PreviewCard
import me.rerere.tension.ui.screen.destinations.PickScreenDestination
import me.rerere.tension.ui.states.rememberStringSetPreference
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
                    var providersSet by rememberStringSetPreference(
                        keyName = SubscribedDataProviderKey,
                        initialValue = emptySet(),
                        defaultValue = emptySet()
                    )

                    IconButton(onClick = {
                        providersSet = providersSet.toMutableSet().apply {
                            add("epic_free_games")
                        }
                    }) {
                        Icon(Icons.Rounded.Face, null)
                    }

                    IconButton(onClick = {
                        navigator.navigate(PickScreenDestination)
                    }) {
                        Icon(Icons.Rounded.Add, null)
                    }
                }
            )
        }
    ) {
        val providers = collectSubscribedDataProviders()
        LazyColumn {
            items(providers) {
                PreviewCard(navigator, it)
            }
        }
    }
}