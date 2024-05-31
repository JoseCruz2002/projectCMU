package pt.ulisboa.tecnico.cmov.frontend.ui.medicine_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun MedicineRoute(
    onSelectPharmacy: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicineViewModel = viewModel(factory = MedicineViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    MedicineScreen(
        medicine = uiState.medicine,
        onSelectPharmacy = onSelectPharmacy,
        modifier = modifier
    )
}

@Composable
fun MedicineScreen(
    medicine: Medicine,
    onSelectPharmacy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .displayCutoutPadding()
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
            Row {
            // Placeholders for image
            AsyncImage(
                model = ImageRequest
                    .Builder(context = LocalContext.current)
                    .data(medicine.img)
                    .crossfade(true)
                    .build(),
                contentDescription = medicine.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(medicine.description, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }
        Text(
            text = stringResource(R.string.medicine_pharmacies),
            style = MaterialTheme.typography.titleMedium
        )
        Column {
            medicine.pharmacies.forEach { pharmacy ->
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(pharmacy.pharmacy.name) },
                    modifier = Modifier.clickable { onSelectPharmacy(pharmacy.pharmacy.id) },
                    trailingContent = { Text(pharmacy.quantity.toString()) }
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
            onSelectPharmacy = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
