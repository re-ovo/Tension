package me.rerere.tension.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import coil.compose.ImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.coilShimmer(imagePainter: ImagePainter) = composed {
    placeholder(
        visible = imagePainter.state is ImagePainter.State.Loading,
        highlight = PlaceholderHighlight.shimmer()
    )
}