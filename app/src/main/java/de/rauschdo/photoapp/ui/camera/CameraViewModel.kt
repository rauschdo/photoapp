package de.rauschdo.photoapp.ui.camera

import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.photoapp.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() :
    BaseViewModel<CameraContract.Action, CameraContract.Navigation, CameraContract.UiState>() {

    private val _uiState = MutableStateFlow(CameraContract.UiState())
    override val viewState: StateFlow<CameraContract.UiState>
        get() = _uiState

    override fun handleActions(action: CameraContract.Action) {
        when (action) {
            else -> Unit
        }
    }
}
