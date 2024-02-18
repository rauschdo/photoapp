package de.rauschdo.photoapp.ui.editor

import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.photoapp.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor() :
    BaseViewModel<EditorContract.Action, EditorContract.Navigation, EditorContract.UiState>() {

    private val _uiState = MutableStateFlow(EditorContract.UiState())
    override val viewState: StateFlow<EditorContract.UiState>
        get() = _uiState

    override fun handleActions(action: EditorContract.Action) {
        when (action) {
            else -> Unit
        }
    }
}
