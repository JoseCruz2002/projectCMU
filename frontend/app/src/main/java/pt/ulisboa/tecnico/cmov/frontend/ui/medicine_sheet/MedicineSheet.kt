package pt.ulisboa.tecnico.cmov.frontend.ui.medicine_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineSheet(
    name: String,
    viewModel: MedicineSheetViewModel = viewModel(),
    pharmacyWhereAvaivable: List<Pharmacy>,
    modifier: Modifier = Modifier
) {
    // Create a modal bottom sheet
    val sheetState = rememberModalBottomSheetState()
    // Remember the coroutine scope
    val scope = rememberCoroutineScope()
    // State to track whether the bottom sheet is expanded or collapsed
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                uiState.showBottomSheet = false
            },
            sheetState = sheetState,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                // Sheet content
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            uiState.showBottomSheet = false
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }
                Text(name, style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                    // TODO Verify that the medicine actually has an image
                    /*Image(
                        painter = painterResource(id = ),
                        contentDescription = stringResource(id = )
                    )*/
                )
                Text(stringResource(R.string.medicine_pharmacies))
                Column {
                    for (pharmacy: Pharmacy in pharmacyWhereAvaivable) {
                        Button(
                            onClick = {
                                // TODO Go to the AddPharmacyScreen
                            },
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                        ) {
                            Text(pharmacy.name)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MedicineSheetPreview() {
    MedicineSheet(
        name = "Benuron",
        pharmacyWhereAvaivable = listOf()
    )
}