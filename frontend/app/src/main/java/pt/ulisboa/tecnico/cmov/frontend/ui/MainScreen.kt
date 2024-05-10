package pt.ulisboa.tecnico.cmov.frontend.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.maps.android.compose.GoogleMap
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun MainScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    MainScreen(
        pharmacies = uiState.pharmacies,
        modifier = modifier
    )
}

@Composable
fun MainScreen(
    pharmacies: List<Pharmacy>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //load map in exact location
            //TODO
        } else {
            //load map in predefined location or last known location
            //TODO
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        onMapLoaded = {requestLocationPermission(context, requestPermissionLauncher)}
    ) {

    }
}

private fun requestLocationPermission(context: Context, launcher: ActivityResultLauncher<String>) =
    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            //load map in exact location
            //TODO
        }
        else -> {
            // Request permission if not already granted
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PharmacISTTheme {
        MainScreen(
            pharmacies = listOf(
                Pharmacy("myPharmacy", "Lisbon", "", mapOf()),
                Pharmacy("Other Pharmacy", "Lisbon", "", mapOf()),
            ),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}