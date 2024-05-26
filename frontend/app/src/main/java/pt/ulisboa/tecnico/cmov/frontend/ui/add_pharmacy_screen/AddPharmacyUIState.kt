package pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

data class AddPharmacyUIState(
    val name: String = "",
    val address: String = "",
    val imageUri: Uri = Uri.EMPTY,
    val currentLocation: LatLng = LatLng( 0.0, 0.0),
    val chosenLocation: LatLng = LatLng(0.0, 0.0),
    val userChoseLocation: Boolean = false
)
