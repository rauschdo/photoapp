@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package de.rauschdo.photoapp.ui.editor

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rauschdo.photoapp.R
import de.rauschdo.photoapp.architecture.LAUNCH_EVENTS_FOR_NAVIGATION
import de.rauschdo.photoapp.data.EditType
import de.rauschdo.photoapp.data.FilterType
import de.rauschdo.photoapp.data.Frame
import de.rauschdo.photoapp.ui.navigation.AppNav
import de.rauschdo.photoapp.ui.navigation.AppNavDest
import de.rauschdo.photoapp.ui.navigation.navRoute
import de.rauschdo.photoapp.ui.theme.TypeDefinition
import de.rauschdo.photoapp.utility.circularReveal
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorDestination(
    navigator: AppNav,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val state: EditorContract.UiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(LAUNCH_EVENTS_FOR_NAVIGATION) {
        viewModel.navigator.onEach { navigationRequest ->
            when (navigationRequest) {
                EditorContract.Navigation.ToHome -> navigator.popUpTo(AppNavDest.Home.navRoute())
            }
        }.collect()
    }
    EditorScreen(
        state = state,
        forwardAction = { action -> viewModel.setAction(action) }
    )
}

@Composable
private fun EditorScreen(
    state: EditorContract.UiState,
    forwardAction: (EditorContract.Action) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var accentColors = listOf(
        Color(0xFF7ACA8F),
        Color(0xFFa783bc),
        Color(0xFFF77262),
        Color(0xFFFFCF01)
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top = 0),
                title = { Text(stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { forwardAction(EditorContract.Action.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { forwardAction(EditorContract.Action.OnSaveClicked) }) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Button"
                        )
                    }
                }
            )
        }
    ) {
        Box {
            // ***************************** Main
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Row {
                    val accentModifierBase: Modifier = if (screenWidth > 400.dp) {
                        accentColors = accentColors.plus(accentColors)
                        Modifier.weight(1f)
                    } else {
                        Modifier.width(100.dp)
                    }
                    accentColors.forEachIndexed { _, color ->
                        Box(
                            modifier = accentModifierBase
                                .height(6.dp)
                                .background(color)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Box for frame + image + text
                ComposableToBitmap(
                    isActive = state.bitmapCreatorActive,
                    onBitmapCreated = { forwardAction(EditorContract.Action.OnBitmapCreated(it)) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(
                                width = 300.dp,
                                height = 340.dp
                            )
                            .align(Alignment.CenterHorizontally)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = state.selectedFrame != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            state.selectedFrame?.let {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = state.selectedFrame),
                                    contentDescription = "Image frame"
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .padding(top = 12.dp)
                                .align(Alignment.TopCenter)
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                bitmap = (state.photo ?: Bitmap.createBitmap(
                                    1,
                                    1,
                                    Bitmap.Config.ARGB_8888
                                ))
                                    .asImageBitmap(),
                                contentDescription = "Image to edit",
                                contentScale = ContentScale.Crop
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            visible = state.caption != null
                        ) {
                            Text(
                                modifier = Modifier,
                                text = state.caption!!
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // ***************************** EditType Row
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    EditType.entries.forEach {
                        IconButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                forwardAction(
                                    EditorContract.Action.OnEditorItemClicked(
                                        it
                                    )
                                )
                            }
                        ) {
                            Icon(imageVector = it.icon, contentDescription = it.name)
                        }
                    }
                }
            }

            // ***************************** EditType Contents
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 56.dp)
            ) {
                // ***************************** Filter
                EditorActionBox(
                    modifier = Modifier,
                    activeEditType = state.activeEditType,
                    editType = EditType.FILTER
                ) {
                    val filterScrollState = rememberScrollState()
                    Row(modifier = Modifier.horizontalScroll(filterScrollState)) {
                        FilterType.clickToApplyFilters().forEach { filter ->
                            FilterButton(
                                modifier = Modifier.minimumInteractiveComponentSize(),
                                painter = painterResource(id = R.drawable.air_ballon),
                                filterType = filter,
                                onClicked = {
                                    forwardAction(
                                        EditorContract.Action.OnFilterClicked(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

                // ***************************** Brightness
                EditorActionBox(
                    modifier = Modifier,
                    activeEditType = state.activeEditType,
                    editType = EditType.BRIGHTNESS
                ) {
                    Row(modifier = Modifier) {
                        Text(text = "Brightness")
                        Slider(
                            valueRange = 0f..5f,
                            value = state.brightness,
                            onValueChange = {
                                forwardAction(
                                    EditorContract.Action.OnBrightnessChanged(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }

                // ***************************** Frames
                EditorActionBox(
                    modifier = Modifier,
                    activeEditType = state.activeEditType,
                    editType = EditType.FRAME
                ) {
                    val filterScrollState = rememberScrollState()
                    Row(modifier = Modifier.horizontalScroll(filterScrollState)) {
                        Frame.entries.forEach { frame ->
                            FrameButton(
                                modifier = Modifier.minimumInteractiveComponentSize(),
                                painter = painterResource(id = R.drawable.air_ballon),
                                frame = frame,
                                onClicked = {
                                    forwardAction(
                                        EditorContract.Action.OnFrameClicked(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

                // ***************************** Caption
                EditorActionBox(
                    modifier = Modifier,
                    activeEditType = state.activeEditType,
                    editType = EditType.CAPTION
                ) {
                    val captionState = rememberTextFieldState(initialText = "")
                    Column {
                        BasicTextField2(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            textStyle = TextStyle(fontSize = TypeDefinition.bodySmall.fontSize),
                            lineLimits = TextFieldLineLimits.SingleLine,
                            state = captionState
                        )
                        TextButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                forwardAction(
                                    EditorContract.Action.OnApplyCaptionClicked(
                                        captionState.text.toString()
                                    )
                                )
                                captionState.edit { revertAllChanges() }
                            }
                        ) {
                            Text(text = "Apply Caption")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComposableToBitmap(
    isActive: Boolean,
    onBitmapCreated: (ImageBitmap) -> Unit,
    content: @Composable () -> Unit,
) {
    if (isActive) {
        val view = LocalView.current
        // Measure and layout the composable
        val size = IntSize(view.width, view.height)
        val image =
            Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888).asImageBitmap()
        val canvas = Canvas(image)
        content()
        view.draw(canvas.nativeCanvas)
        // Convert the bitmap to ImageBitmap
        onBitmapCreated(image)
    } else {
        content()
    }
}

/**
 * Content that animates in when given [EditType] is active in state
 */
@Composable
private fun EditorActionBox(
    modifier: Modifier = Modifier,
    activeEditType: EditType?,
    editType: EditType,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .circularReveal(isVisible = activeEditType == editType)
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // We don't want to interact with the content when its not seen
        // but we have to keep the box at all times to keep animation smooth
        AnimatedVisibility(
            visible = activeEditType == editType,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            content()
        }
    }
}

@Composable
private fun FilterButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    filterType: FilterType,
    onClicked: (FilterType) -> Unit
) {
    Image(
        modifier = modifier
            .size(56.dp)
            .clickable { onClicked(filterType) },
        painter = painter,
        contentDescription = contentDescription
    )
}

@Composable
private fun FrameButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    frame: Frame,
    onClicked: (Frame) -> Unit
) {
    Image(
        modifier = modifier
            .size(56.dp)
            .clickable { onClicked(frame) },
        painter = painter,
        contentDescription = contentDescription
    )
}

@Composable
private fun ScreenPreview() {
    EditorScreen(
        state = EditorContract.UiState(),
        forwardAction = {}
    )
}
