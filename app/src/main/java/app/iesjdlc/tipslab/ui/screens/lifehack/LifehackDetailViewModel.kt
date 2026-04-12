package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LifehackDetailViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(LifehackDetailUiState())
    val uiState: StateFlow<LifehackDetailUiState> = _uiState.asStateFlow()
}