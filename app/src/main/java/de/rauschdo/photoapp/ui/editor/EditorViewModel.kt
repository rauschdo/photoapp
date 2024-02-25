package de.rauschdo.photoapp.ui.editor

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.photoapp.architecture.BaseViewModel
import de.rauschdo.photoapp.data.FilterType
import de.rauschdo.photoapp.utility.BitmapController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val bitmapController: BitmapController
) : BaseViewModel<EditorContract.Action, EditorContract.Navigation, EditorContract.UiState>() {

    private val _uiState = MutableStateFlow(EditorContract.UiState())
    override val viewState: StateFlow<EditorContract.UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            bitmapController.captureHolder.collectLatest { bitmap ->
                _uiState.update { it.copy(photo = bitmap) }
            }
        }
    }

    override fun handleActions(action: EditorContract.Action) {
        when (action) {
            is EditorContract.Action.OnBackClicked -> requestNavigation(EditorContract.Navigation.ToHome)
            is EditorContract.Action.OnApplyCaptionClicked -> {
                _uiState.update { it.copy(caption = action.caption) }
            }

            is EditorContract.Action.OnBitmapCreated -> onBitmapCreated(action.bitmap)
            is EditorContract.Action.OnBrightnessChanged -> onBrightnessChanged(action.brightness)
            is EditorContract.Action.OnEditorItemClicked -> {
                if (_uiState.value.activeEditType == action.editType) {
                    _uiState.update { it.copy(activeEditType = null) }
                } else {
                    _uiState.update { it.copy(activeEditType = action.editType) }
                }
            }

            is EditorContract.Action.OnFilterClicked -> onFilterClicked(action.filterType)
            is EditorContract.Action.OnFrameClicked -> {
                _uiState.update { it.copy(selectedFrame = action.frame?.resourceId) }
            }

            is EditorContract.Action.OnSaveClicked -> onSaveClicked()
        }
    }

    private fun onBitmapCreated(bitmap: ImageBitmap) {
        viewModelScope.launch {
            bitmapController.saveEditorImage(
                filename = UUID.randomUUID().toString(),
                bitmap = bitmap.asAndroidBitmap()
            )
        }
    }

    private fun onBrightnessChanged(brightness: Float) {
        _uiState.update { it.copy(brightness = brightness) }
        // TODO Apply filter
    }

    private fun onFilterClicked(filterType: FilterType) {
        when (filterType) {
            else -> Unit // TODO
        }
    }

    private fun onSaveClicked() {
        // activate wrapper composable that should create bitmap
        _uiState.update { it.copy(bitmapCreatorActive = true) }
    }
}
