package me.rerere.tension.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import me.rerere.tension.R

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier
) {
    val anim by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.cube_loader)
    )
    LottieAnimation(
        modifier = modifier,
        composition = anim,
        iterations = LottieConstants.IterateForever
    )
}