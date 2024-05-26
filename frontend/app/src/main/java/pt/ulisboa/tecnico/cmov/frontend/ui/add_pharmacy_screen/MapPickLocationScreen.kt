package pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapPickLocationRoute(
    backToAddPharmacy: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPharmacyViewModel
) {
    MapPickLocationScreen(
        onMapClick = {
            viewModel.updateChosenLocation(it)
            viewModel.updateUserChoseLocation(true)
            backToAddPharmacy()
        },
        modifier = modifier,
        viewModel = viewModel
    )
}

@Composable
fun MapPickLocationScreen(
    onMapClick: (LatLng) -> Unit,
    modifier: Modifier,
    viewModel: AddPharmacyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // The state.currentLocation already has either the user location or the default one
    val cameraPositionState = CameraPositionState(
        /*CameraPosition(uiState.currentLocation, 10.0F, 0.0F, 0.0F)*/
        CameraPosition(LatLng(38.736946, -9.142685), 15f, 0f, 0f)
    )

    GoogleMap(
        modifier = modifier,
        onMapClick = onMapClick,
        cameraPositionState = cameraPositionState
    )
}