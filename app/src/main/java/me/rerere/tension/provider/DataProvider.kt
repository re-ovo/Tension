package me.rerere.tension.provider

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

interface DataProvider {
    fun getId(): String

    fun getDisplayName(): String

    fun getCategory(): Category

    @Composable
    fun getPreviewUI(navigator: DestinationsNavigator): @Composable () -> Unit

    @Composable
    fun getDetailUI(navigator: DestinationsNavigator): @Composable () -> Unit
}