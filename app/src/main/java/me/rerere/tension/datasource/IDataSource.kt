package me.rerere.tension.datasource

import androidx.compose.runtime.Composable

interface IDataSource {
    fun getCategory(): Category

    @Composable
    fun getPreviewUI(): @Composable () -> Unit

    @Composable
    fun getDetailUI(): @Composable () -> Unit
}