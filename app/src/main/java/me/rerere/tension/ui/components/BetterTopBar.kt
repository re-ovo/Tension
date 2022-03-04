package me.rerere.tension.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Md3TopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    appBarStyle: AppBarStyle = AppBarStyle.Small,
    scrollBehavior: TopAppBarScrollBehavior? = null
){
    val scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    val appBarContainerColor by colors.containerColor(scrollFraction)

    Surface(modifier = modifier, color = appBarContainerColor) {
        when(appBarStyle){
            AppBarStyle.Small -> {
                SmallTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
            AppBarStyle.Medium -> {
                MediumTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
            AppBarStyle.Large -> {
                LargeTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
        }
    }
}

@Composable
fun BackIcon(navigator: DestinationsNavigator) {
    IconButton(onClick = { navigator.popBackStack() }) {
        Icon(Icons.Rounded.ArrowBack, null)
    }
}

enum class AppBarStyle {
    Small,
    Medium,
    Large
}