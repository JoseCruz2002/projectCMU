package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineSheet(
    name: String,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = { /*TODO*/ },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.medicine_pharmacies))
        }

    }
}

@Preview
@Composable
fun MedicineSheetPreview() {
    MedicineSheet(
        name = "Benuron"
    )
}