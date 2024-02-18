package de.rauschdo.photoapp.ui.home

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
            HomeContract.Action.OnCameraClicked -> requestNavigation(HomeContract.Navigation.ToCamera)

            is HomeContract.Action.OnGalleryClicked -> _uiState.update { it.copy(launchContract = true) }
            is HomeContract.Action.ConsumeContract -> _uiState.update { it.copy(launchContract = false) }
        }
    }


    // TODO code below should be placed in camera Fragment once integrating camera(X) Screen
//    private fun scaleBitmapToMaxSize(
//        maxSize: Int,
//        bm: Bitmap
//    ): Bitmap {
//        val outWidth: Int
//        val outHeight: Int
//        val inWidth = bm.width
//        val inHeight = bm.height
//        if (inWidth > inHeight) {
//            outWidth = maxSize
//            outHeight = inHeight * maxSize / inWidth
//        } else {
//            outHeight = maxSize
//            outWidth = inWidth * maxSize / inHeight
//        }
//        return Bitmap.createScaledBitmap(bm, outWidth, outHeight, false)
//    }
//
//    private fun passBitmapToOtherActivity(bitmap: Bitmap): String? {
//
//        // Save Bitmap into the Device (to pass it to the other Activity)
//        var fileName: String? = "imagePassed"
//        try {
//            val bytes = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//            val fo = openFileOutput(fileName, AppCompatActivity.MODE_PRIVATE)
//            fo.write(bytes.toByteArray())
//            fo.close()
//
//            // Go to CreateMeme
//            val i = Intent(this@MainActivity, ImageEditor::class.java)
//            startActivity(i)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            fileName = null
//        }
//        return fileName
//    }
}