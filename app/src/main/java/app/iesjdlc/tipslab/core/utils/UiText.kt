package app.iesjdlc.tipslab.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class StringRes(val resId: Int) : UiText()
    class StringResWithArgs(val resId: Int, vararg val args: Any) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is StringRes -> stringResource(resId)
        is StringResWithArgs -> stringResource(resId, *args)
    }
}