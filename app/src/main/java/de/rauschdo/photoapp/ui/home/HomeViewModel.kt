package de.rauschdo.photoapp.ui.home

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import dagger.hilt.android.lifecycle.HiltViewModel
import de.rauschdo.photoapp.architecture.BaseViewModel
import de.rauschdo.photoapp.utility.BitmapController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bitmapController: BitmapController
) : BaseViewModel<HomeContract.Action, HomeContract.Navigation, HomeContract.UiState>() {

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    override val viewState: StateFlow<HomeContract.UiState>
        get() = _uiState

    override fun handleActions(action: HomeContract.Action) {
        when (action) {
            is HomeContract.Action.OnImagePicked -> onImagePicked(action.uri)
            is HomeContract.Action.OnGalleryClicked -> _uiState.update { it.copy(launchContract = true) }
            is HomeContract.Action.ConsumeContract -> _uiState.update { it.copy(launchContract = false) }
            is HomeContract.Action.OnCameraClicked -> requestNavigation(HomeContract.Navigation.ToCamera)
        }
    }

    private fun onImagePicked(uri: Uri) {
        var bitmap: Bitmap? = null
        try {
            val contentResolver: ContentResolver = bitmapController.context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        }
        // Expected: FileNotFoundException, IOException
        catch (e: Exception) {
            e.printStackTrace()
        }
        bitmap?.let {
            bitmapController.captureHolder.update { bitmap }
            requestNavigation(HomeContract.Navigation.ToImageEditor)
        } ?: Toast.makeText(
            bitmapController.context,
            "Couldn't process picked image",
            Toast.LENGTH_SHORT
        ).show()
    }
}