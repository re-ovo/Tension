package me.rerere.tension.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.tension.provider.DataProvider
import me.rerere.tension.provider.SubscribedDataProviderKey
import me.rerere.tension.ui.states.rememberStringSetPreference

@Composable
fun PreviewCard(navigator: DestinationsNavigator, provider: DataProvider) {
    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = provider.getDisplayName(),
                    style = MaterialTheme.typography.headlineSmall
                )
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = !expanded }) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        var providersSet by rememberStringSetPreference(
                            keyName = SubscribedDataProviderKey,
                            initialValue = emptySet(),
                            defaultValue = emptySet()
                        )
                        DropdownMenuItem(
                            text = {
                                Text("删除")
                            },
                            onClick = {
                                expanded = false
                                providersSet = providersSet.toMutableSet().apply {
                                    remove(provider.getId())
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text("编辑")
                            },
                            onClick = {
                                expanded = false
                            }
                        )
                    }
                    Icon(Icons.Rounded.MoreVert, null)
                }
            }
            provider.getPreviewUI(navigator).invoke()
        }
    }
}