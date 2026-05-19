package app.iesjdlc.tipslab.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon

@Composable
fun AnimatedCircleButton(
    isActive: Boolean,
    onClick: () -> Unit,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    contentDescription: String
) {
    var showBorderModifier by remember { mutableStateOf(!isActive) }

    LaunchedEffect(isActive) {
        if (!isActive) showBorderModifier = true
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )

    val iconColor by animateColorAsState(
        targetValue = if (isActive) Color.White else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 200)
    )

    val borderColor by animateColorAsState(
        targetValue = if (isActive) Color.Transparent else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 200),
        finishedListener = { finalColor ->
            if (finalColor == Color.Transparent) showBorderModifier = false
        }
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .then(
                if (showBorderModifier) Modifier.border(1.dp, borderColor, CircleShape)
                else Modifier
            )
    ) {
        Icon(
            imageVector = if (isActive) activeIcon else inactiveIcon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}