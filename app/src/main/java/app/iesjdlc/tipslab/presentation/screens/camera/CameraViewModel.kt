package app.iesjdlc.tipslab.presentation.screens.camera

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val allowVideo = savedStateHandle.toRoute<Route.Camera>().allowVideo
}
