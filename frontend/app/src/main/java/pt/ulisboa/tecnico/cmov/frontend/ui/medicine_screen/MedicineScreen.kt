package pt.ulisboa.tecnico.cmov.frontend.ui.medicine_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun MedicineRoute(
    modifier: Modifier = Modifier,
    viewModel: MedicineViewModel = viewModel(factory = MedicineViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    MedicineScreen(
        medicine = uiState.medicine,
        pharmacies = emptyList(),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineScreen(
    medicine: Medicine,
    pharmacies: List<Pharmacy>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = medicine.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.image_height))
        ) {
        }
        Text(
            text = stringResource(R.string.medicine_pharmacies),
            style = MaterialTheme.typography.titleMedium
        )
        Column {
            pharmacies.forEach { pharmacy ->
                HorizontalDivider()
                ListItem(
                    headlineContent = {
                        Text(pharmacy.name)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicineScreenPreview() {
    PharmacISTTheme {
        MedicineScreen(
            medicine = Medicine(
                name = "Benuron",
                description = "Saves lives",
                img = "",
                pharmacies = emptyList()
            ),
            pharmacies = listOf(
                Pharmacy(name = "People's Pharmacy"),
                Pharmacy(name = "TopMeds"),
                Pharmacy(name = "PharmaLife")
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
