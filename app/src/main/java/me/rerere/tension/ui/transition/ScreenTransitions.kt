package me.rerere.tension.ui.transition

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations

val DefaultScreenTransition by lazy {
    RootNavGraphDefaultAnimations(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = {
                    it
                },
                animationSpec = tween()
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = {
                    -it
                },
                animationSpec = tween()
            ) + fadeOut(
                animationSpec = tween()
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = {
                    -it
                },
                animationSpec = tween()
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = {
                    it
                },
                animationSpec = tween()
            )
        }
    )
}