package pt.ulisboa.tecnico.cmov.frontend.ui.main_screen

import android.location.Location
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.components.defaultLocation

data class MainScreenUIState(
    val pharmacies: List<Pharmacy>,
    val favourites: List<String>,
    val query: String = "",
    val active: Boolean = false,
    val currentLocation : Location = defaultLocation,
    val locationPermission: Boolean = false,
    val goToLocation: Boolean = true,
    val justSearchedLocation: Boolean = false
)
