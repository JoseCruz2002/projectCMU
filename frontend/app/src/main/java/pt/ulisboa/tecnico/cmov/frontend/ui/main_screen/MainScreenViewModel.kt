package pt.ulisboa.tecnico.cmov.frontend.ui.main_screen

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.PharmacyRepository
import pt.ulisboa.tecnico.cmov.frontend.data.UserRepository
import pt.ulisboa.tecnico.cmov.frontend.ui.components.getLatLngFromPlace
import pt.ulisboa.tecnico.cmov.frontend.ui.components.getLocationFromLatLng

class MainScreenViewModel(
    private val pharmacyRepository: PharmacyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUIState(listOf(), listOf()))
    val uiState: StateFlow<MainScreenUIState> = _uiState.asStateFlow()

    init {
        getPharmacies()
        getFavourites()
    }

    private fun getPharmacies() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    pharmacies = pharmacyRepository.getPharmacies()
                )
            }
        }
    }

    private fun getFavourites() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    favourites = userRepository.getFavorites()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val pharmacyRepository = application.container.pharmacyRepository
                val userRepository = application.container.userRepository
                MainScreenViewModel(pharmacyRepository = pharmacyRepository, userRepository = userRepository)
            }
        }
    }

    fun updateQuery(newQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = newQuery
            )
        }
    }

    fun updateActive(newActive: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                active = newActive
            )
        }
    }

    fun updateLocation(newLocation: Location) {
        _uiState.update { currentState ->
            currentState.copy(
                currentLocation = newLocation
            )
        }
    }

    fun updateLocationPermission(newPermission: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                locationPermission = newPermission
            )
        }
    }

    fun updateGoToLocation(newGoToLocation: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                goToLocation = newGoToLocation
            )
        }
    }

    fun updateJustSearchedLocation(newJustSearchedLocation: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                justSearchedLocation = newJustSearchedLocation
            )
        }
    }

    suspend fun searchLocation(query: String, apikey: String) {
        val latLng: LatLng? = getLatLngFromPlace(query, apikey)
        updateJustSearchedLocation(true)
        updateLocation(getLocationFromLatLng(LatLng(latLng!!.latitude, latLng.longitude)))
    }

}