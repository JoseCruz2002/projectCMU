package pt.ulisboa.tecnico.cmov.frontend.ui.components

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.LocationSource

private const val TAG = "LocationSource"

/**
 * A [LocationSource] which allows it's location to be set. In practice you would request location
 * updates (https://developer.android.com/training/location/request-updates).
 */
class MyLocationSource(private val context: Context) : LocationSource {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setIntervalMillis(5000) // Sets the interval for location updates
            .setMinUpdateIntervalMillis(5000 / 2) // Sets the fastest allowed interval of location updates.
            .setWaitForAccurateLocation(true) // Want Accurate location updates make it true or you get approximate updates
            .setMaxUpdateDelayMillis(5000) // Sets the longest a location update may be delayed.
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