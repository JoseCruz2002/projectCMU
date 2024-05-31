package pt.ulisboa.tecnico.cmov.frontend.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.main_screen.MainScreenViewModel

private const val TAG = "LocationTrackActivity"
private const val zoom = 15f
private val defaultLatLng = LatLng(38.434169, -9.188746)
private val defaultCameraPosition = CameraPosition.fromLatLngZoom(defaultLatLng, zoom)
val defaultLocation = getLocationFromLatLng(defaultLatLng)

@Composable
fun CreateMap(
    viewModel: MainScreenViewModel,
    modifier: Modifier,
    onPharmacyClick: (String) -> Unit,
) {
    // Context
    val context = LocalContext.current

    // The state of the main screen, has my location and other flags
    val uiState by viewModel.uiState.collectAsState()

    // To set the blue dot of location to appear
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    // To control and observe the map camera
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    // Ly location source to pass to the Google Map composable
    val locationSource = remember { MyLocationSource(context= context) }

    // Request location permission
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.updateLocationPermission(isGranted)
    }

    fun chooseColor(pharmacy: Pharmacy): BitmapDescriptor {
        var tmp = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)

        if (uiState.favourites.contains(pharmacy.id)) {
            // Create a bitmap descriptor with a specific color
            tmp = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
        }

        return tmp
    }

    LaunchedEffect(true) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
            viewModel.updateLocationPermission(true)
        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Observe location updates and update the camera position
    LaunchedEffect(uiState.locationPermission) {
        if (uiState.locationPermission && uiState.goToLocation) {
            locationSource.activate { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                Log.d(TAG, "Location update: $latLng")
                viewModel.updateLocation(location)
            }
        }
    }

    LaunchedEffect(uiState.currentLocation) {
        if (uiState.goToLocation) {
            val latLng = LatLng(
                uiState.currentLocation.latitude,
                uiState.currentLocation.longitude
            )
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            if (uiState.justSearchedLocation) {
                viewModel.updateGoToLocation(false)
                viewModel.updateJustSearchedLocation(false)
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving &&
            cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
            if (uiState.goToLocation && uiState.locationPermission) {
                viewModel.updateGoToLocation(false)
                locationSource.deactivate()
            }
        }
    }

    LaunchedEffect(uiState.justSearchedLocation) {
        if (uiState.justSearchedLocation && !uiState.goToLocation && uiState.locationPermission) {
            viewModel.updateGoToLocation(true)
        }
    }

    // Only changes from false to true when the location button is pressed or a location is searched
    LaunchedEffect(uiState.goToLocation) {
        if (uiState.goToLocation && uiState.locationPermission && !uiState.justSearchedLocation) {
            locationSource.activate { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                Log.d(TAG, "Location update: $latLng")
                viewModel.updateLocation(location)
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = { },
        onMyLocationButtonClick = {
            viewModel.updateGoToLocation(true)
            true
        },
        locationSource = locationSource,
        properties = mapProperties
    ) {
        uiState.pharmacies.forEach { pharmacy ->
            Marker(
                contentDescription = pharmacy.name,
                state = MarkerState(LatLng(pharmacy.latitude, pharmacy.longitude)),
                onClick = {
                    onPharmacyClick(pharmacy.id)
                    true
                },
                icon = chooseColor(pharmacy)
            )
        }
    }
}

public fun getLocationFromLatLng(latLng: LatLng): Location {
    val newLocation: Location = Location("")
    newLocation.latitude = latLng.latitude
    newLocation.longitude = latLng.longitude
    return newLocation
}
