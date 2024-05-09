package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    GoogleMap(
        modifier = Modifier.fillMaxSize()
    ) {

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