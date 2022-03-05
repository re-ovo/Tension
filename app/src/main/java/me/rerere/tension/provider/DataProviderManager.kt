package me.rerere.tension.provider

import androidx.compose.runtime.Composable
import me.rerere.tension.provider.game.EpicGameSource
import me.rerere.tension.ui.states.rememberStringSetPreference

val SubscribedDataProviderKey = "subscribed_data_providers"

object DataProviderManager {
    fun getDataProviderById(id: String): DataProvider? = when (id) {
        "epic_free_games" -> EpicGameSource()
        else -> null
    }
}

@Composable
fun collectSubscribedDataProviders(): List<DataProvider> = rememberStringSetPreference(
    keyName = SubscribedDataProviderKey,
    initialValue = emptySet(),
    defaultValue = emptySet()
).value.mapNotNull { DataProviderManager.getDataProviderById(it) }