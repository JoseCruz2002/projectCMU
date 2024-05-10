package pt.ulisboa.tecnico.cmov.frontend.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

private const val TAG = "LocationTrackActivity"
private const val zoom = 15f
private val defaultLatLng = LatLng(38.734169, -9.188746)
private val defaultCameraPosition = CameraPosition.fromLatLngZoom(defaultLatLng, zoom)
private val defaultLocation = getLocationFromLatLng(defaultLatLng)

/**
 * This shows how to use a custom location source to show a blue dot on the map based on your own
 * locations.
 */
class MapComposeAPI() {

    var permissionGranted: Boolean? = true
    var hadPermission: Boolean = true

    @Composable
    fun InitiateMap(
        pharmacies: List<Pharmacy>,
        onPharmacyClick: (String) -> Unit
    ) {
        val context = LocalContext.current
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                permissionGranted = true
            } else {
                // Permission denied, explain to the user
                Toast.makeText(
                    context,
                    "Location permission is required to track users location",
                    Toast.LENGTH_SHORT
                ).show()
                permissionGranted = false
            }
        }
        requestLocationPermission(
            context = context,
            launcher = requestPermissionLauncher,
            onPharmacyClick = onPharmacyClick,
            pharmacies = pharmacies
        )
        if (!hadPermission) {
            CreateMap(
                locPermissionGranted = permissionGranted!!,
                pharmacies = pharmacies,
                onPharmacyClick = onPharmacyClick
            )
        }
    }

    @Composable
    fun CreateMap(
        locPermissionGranted: Boolean,
        pharmacies: List<Pharmacy>,
        onPharmacyClick: (String) -> Unit
    ) {

        Log.d(TAG, "Location Permission: $locPermissionGranted")

        var isMapLoaded by remember { mutableStateOf(false) }

        // To control and observe the map camera
        val cameraPositionState = rememberCameraPositionState {
            position = defaultCameraPosition
        }

        // To show blue dot on map
        val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = locPermissionGranted)) }

        // Use a MutableState to hold the current location
        val locationSource = MyLocationSource(LocalContext.current)
        var locationState by remember { mutableStateOf(defaultLocation) }
        val activatedState by remember { mutableStateOf(false) }

        if (locPermissionGranted) {
            LaunchedEffect(activatedState) {
                locationSource.activate { location ->
                    // Update the current location state
                    locationState = location
                }
            }

            // Update blue dot and camera when the location changes
            LaunchedEffect(locationState) {
                Log.d(TAG, "Updating blue dot on map...")
                locationSource.onLocationChanged(locationState)

                Log.d(TAG, "Updating camera position...")
                val cameraPosition = CameraPosition.fromLatLngZoom(
                    LatLng(
                        locationState.latitude,
                        locationState.longitude
                    ), zoom
                )
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    1_000
                )
            }
        }

        // Detect when the map starts moving and print the reason
        LaunchedEffect(cameraPositionState.isMoving)
        {
            if (cameraPositionState.isMoving) {
                Log.d(
                    TAG,
                    "Map camera started moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
                )
            }
            if (cameraPositionState.isMoving && permissionGranted != null && permissionGranted!! &&
                cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE
            ) {
                //locationSource.deactivate()
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded =
            {
                isMapLoaded = true
                //requestLocationPermission(context, requestPermissionLauncher)
            },
            // This listener overrides the behavior for the location button. It is intended to be used when a
            // custom behavior is needed.
            onMyLocationButtonClick = {
                Log.d(
                    TAG,
                    "Overriding the onMyLocationButtonClick with this Log"
                ); true
            },
            locationSource = locationSource,
            properties = mapProperties
        ) {
            pharmacies.forEach { pharmacy ->
                Marker(
                    contentDescription = pharmacy.name,
                    state = MarkerState(LatLng(pharmacy.latitude, pharmacy.longitude)),
                    onClick = {
                        onPharmacyClick(pharmacy.id)
                        true
                    }
                )
            }
        }
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {

            }
        }
    }

    @Composable
    private fun requestLocationPermission(
        context: Context,
        pharmacies: List<Pharmacy>,
        onPharmacyClick: (String) -> Unit,
        launcher: ActivityResultLauncher<String>
    ) =
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "Already had location permissions")
                CreateMap(
                    locPermissionGranted = true,
                    pharmacies = pharmacies,
                    onPharmacyClick = onPharmacyClick
                )
            }

            else -> {
                // Request permission if not already granted
                hadPermission = false
                Log.d(TAG, "Asking for location permissions")
                SideEffect {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
}

/**
 * A [LocationSource] which allows it's location to be set. In practice you would request location
 * updates (https://developer.android.com/training/location/request-updates).
 */
private class MyLocationSource(private val context: Context) : LocationSource {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var listener: OnLocationChangedListener? = null

    override fun activate(listener: OnLocationChangedListener) {
        this.listener = listener
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setIntervalMillis(100000) // Sets the interval for location updates
            .setMinUpdateIntervalMillis(100000 / 2) // Sets the fastest allowed interval of location updates.
            .setWaitForAccurateLocation(true) // Want Accurate location updates make it true or you get approximate updates
            .setMaxUpdateDelayMillis(100000) // Sets the longest a location update may be delayed.
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d(TAG, "New location received: $location")
                    listener.onLocationChanged(location)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!, /*Looper.myLooper()*/
                null
            )
            Log.d(TAG, "Location updates requested")
        } catch (e: SecurityException) {
            // Handle permission denial or other location-related exceptions
            Log.e(TAG, "Error requesting location updates: ${e.message}")
        }
    }

    override fun deactivate() {
        fusedLocationClient.removeLocationUpdates(locationCallback!!)
        listener = null
        Log.d(TAG, "Location Source deactivated")
    }

    fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged called with location: $location")
        listener?.onLocationChanged(location)
    }
}

private fun getLocationFromLatLng(latLng: LatLng): Location {
    val newLocation: Location = Location("")
    newLocation.latitude = latLng.latitude
    newLocation.longitude = latLng.longitude
    return newLocation
}

/**
 * State of the map, just holds the location
 */
data class MapComposeAPIUIState(
    val currentLocation: Location
)
