package pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.PharmacyRepository
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.components.getAddressFromCoordinates
import pt.ulisboa.tecnico.cmov.frontend.ui.components.getLatLngFromPlace

const val TAG: String = "addPharmacy"

class AddPharmacyViewModel(private val pharmacyRepository: PharmacyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPharmacyUIState())
    val uiState: StateFlow<AddPharmacyUIState> = _uiState.asStateFlow()

    fun updateName(updatedName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = updatedName
            )
        }
    }

    fun updateAddress(updatedAddress: String) {
        _uiState.update { currentState ->
            currentState.copy(
                address = updatedAddress
            )
        }
    }

    fun updateUri(updatedUri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                imageUri = updatedUri
            )
        }
    }

    fun updateCurrentLocation(newLocation: LatLng) {
        _uiState.update { currentState ->
            currentState.copy(
                currentLocation = newLocation
            )
        }
    }

    fun updateChosenLocation(newChosenLocation: LatLng) {
        _uiState.update { currentState ->
            currentState.copy(
                chosenLocation = newChosenLocation
            )
        }
    }

    fun updateUserChoseLocation(newUserChoseLocation: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                userChoseLocation = newUserChoseLocation
            )
        }
    }

    fun addPharmacy(apiKey: String, coroutineScope: CoroutineScope) {
        viewModelScope.launch {

            val latLng: LatLng?
            if (_uiState.value.userChoseLocation) {
                latLng = _uiState.value.chosenLocation
                coroutineScope.async {
                    getAddressFromCoordinates(latLng.latitude, latLng.longitude, apiKey)
                }.await()?.let {
                    updateAddress(it)
                }
            } else {
                latLng = coroutineScope.async {
                    getLatLngFromPlace(_uiState.value.address, apiKey)
                }.await()
            }
            Log.d(TAG, "coordinates: $latLng and address: ${_uiState.value.address}")

            pharmacyRepository.addPharmacy(
                Pharmacy(
                    name = _uiState.value.name,
                    location = _uiState.value.address,
                    latitude = latLng?.latitude ?: 0.0,
                    longitude = latLng?.longitude ?: 0.0,
                    img = "",
                    medicines = emptyList()
                ),
                uri = _uiState.value.imageUri
            )
            Log.d(TAG, "pharmacy ${_uiState.value.name} added with coordinates: $latLng")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val pharmacyRepository = application.container.pharmacyRepository
                AddPharmacyViewModel(pharmacyRepository = pharmacyRepository)
            }
        }
    }

    /*fun getMyLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            val locationSource = MyLocationSource(context= context)
            locationSource.activate { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                Log.d(TAG, "Location update: $latLng")
                updateCurrentLocation(latLng)
            }
            locationSource.deactivate()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.location_permissions_not_given),
                Toast.LENGTH_SHORT
            ).show()
            updateCurrentLocation(LatLng(39.636995, -9.138543))
        }
    }*/
}