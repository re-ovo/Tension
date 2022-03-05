package me.rerere.tension.provider

import androidx.compose.runtime.Composable

interface DataProvider {
    fun getDisplayName(): String

    fun getCategory(): Category

    @Composable
    fun getPreviewUI(): @Composable () -> Unit

    @Composable
    fun getDetailUI(): @Composable () -> Unit
}