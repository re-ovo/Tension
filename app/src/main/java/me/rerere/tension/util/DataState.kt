package me.rerere.tension.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.rerere.tension.ui.components.CenteredBox

sealed class DataState<out T> {
    fun readSafely(): T? = if (this is Success) value else null

    class Success<T>(val value: T) : DataState<T>()

    class Error(
        val error: String
    ) : DataState<Nothing>()

    object Loading : DataState<Nothing>()

    object Empty : DataState<Nothing>()
}

@Composable
fun <T> DataState<T>.Display(
    onError: @Composable (String) -> Unit = { error ->
        CenteredBox(Modifier.fillMaxSize()) {
            Text(text = "Error: $error")
        }
    },
    onLoading: @Composable () -> Unit = {
        CenteredBox(Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    },
    onSuccess: @Composable (T) -> Unit
) {
    when (this) {
        is DataState.Success -> onSuccess(value)
        is DataState.Error -> onError(error)
        is DataState.Loading -> onLoading()
        is DataState.Empty -> { /* do nothing */
        }
    }
}