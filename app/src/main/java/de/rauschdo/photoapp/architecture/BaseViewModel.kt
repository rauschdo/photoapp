package de.rauschdo.photoapp.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Action : ViewEvent, NavigationEvent : NavigationRequest, UiState : ViewState> :
    ViewModel() {

    /**
     * Main object to propagate updates to UI
     * Each viewModel has to handle initialization by itself
     */
    abstract val viewState: StateFlow<UiState>

    private val _event: MutableSharedFlow<Action> = MutableSharedFlow()
    abstract fun handleActions(action: Action)

    private val _navigationChannel: Channel<NavigationEvent> = Channel()
    val navigator = _navigationChannel.receiveAsFlow()

    private val _permissionChannel: Channel<List<String>> = Channel()
    val permissionHandler = _permissionChannel.receiveAsFlow()

    init {
        subscribeToActions()
    }

    fun setAction(action: Action) {
        viewModelScope.launch { _event.emit(action) }
    }

    private fun subscribeToActions() {
        viewModelScope.launch {
            _event.collect {
                handleActions(it)
            }
        }
    }

    /**
     * Sends [value] into navigationChannel, if implementing composable has a receiver defined
     * it can handle or ignore the sent [value]
     */
    protected fun requestNavigation(value: NavigationEvent, runAfterBlock: (() -> Unit)? = null) =
        viewModelScope.launch {
            _navigationChannel.send(value)
            runAfterBlock?.invoke()
        }

    /**
     * Sends [value] into permissionChannel, the implementing composable should has a receiver defined
     * and is expected to handle the sent [value]
     */
    protected fun requestPermission(permissions: List<String>) =
        viewModelScope.launch { _permissionChannel.send(permissions) }

    open fun onPermissionResult(result: Map<String, Boolean>) {
        // Can be overwritten for custom logic
    }
}