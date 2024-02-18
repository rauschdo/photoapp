package de.rauschdo.photoapp

import android.os.Bundle
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import de.rauschdo.photoapp.ui.main.LocalAppNav
import de.rauschdo.photoapp.ui.main.MainAppState
import de.rauschdo.photoapp.ui.main.rememberMainAppState
import de.rauschdo.photoapp.ui.navigation.MainNavHost
import de.rauschdo.photoapp.ui.theme.PhotoappTheme
import de.rauschdo.photoapp.utility.BitmapController
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bitmapController: BitmapController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PhotoappTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(appState: MainAppState = rememberMainAppState()) {
    val coroutineScope = rememberCoroutineScope()

    PredictiveBackHandler { progress: Flow<BackEventCompat> ->
        try {
            progress.collect { backevent ->
                Timber.tag("back").i(backevent.toString())
            }
            //completion
            if (!appState.navigator.onBackClick()) {
                // TODO app exit dialog or snackbar
            }
        } catch (e: CancellationException) {
            // nothing
        }
    }

    CompositionLocalProvider(LocalAppNav provides appState.navigator) {
        Scaffold {
            MainNavHost(
                modifier = Modifier.padding(it),
                navigator = appState.navigator,
            )
        }
    }
}