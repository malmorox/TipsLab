package app.iesjdlc.tipslab.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

object NavAnimations {
    private const val ANIMATION_DURATION = 500

    // Animación para cuando la pantalla sube desde el borde inferior
    fun slideInVertical() = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(ANIMATION_DURATION)
    )

    // Animación para cuando la pantalla baja hacia el borde inferior
    fun slideOutVertical() = slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(ANIMATION_DURATION)
    )

    fun slideInFromRight() = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(ANIMATION_DURATION)
    )

    fun slideOutToRight() = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(ANIMATION_DURATION)
    )

    fun slideInFromLeft() = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(ANIMATION_DURATION)
    )

    fun slideOutToLeft() = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(ANIMATION_DURATION)
    )
}