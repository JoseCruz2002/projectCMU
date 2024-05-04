package pt.ulisboa.tecnico.cmov.frontend.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddPharmacyRoute(
    modifier: Modifier = Modifier,
    viewModel: AddPharmacyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AddPharmacyScreen(
        name = uiState.name,
        address = uiState.address,
        onNameChange = { viewModel.updateName(it) },
        onAddressChange = { viewModel.updateAddress(it)  },
        modifier = modifier
    )
}

@Composable
fun AddPharmacyScreen(
    name: String,
    address: String,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.add_pharmacy_title),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.pharmacy_name))
                }
            )
            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.pharmacy_address))
                }
            )
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { launchCamera(context) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(Icons.Outlined.PhotoCamera, null)
                    }
                    Text(
                        stringResource(R.string.add_photo),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        ) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f),
                enabled = true
            ) {
                Text(stringResource(R.string.confirm))
            }
        }

    }
}

private fun launchCamera(context: Context) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    // Check if camera app is available
    //if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent) // Use context to start activity
    //} else {
        // Handle the case where no camera app is available
    //    Toast.makeText(context, "No camera app available", Toast.LENGTH_LONG).show()
    //}
}

@Preview(showBackground = true)
@Composable
fun AddPharmacyScreenPreview() {
    PharmacISTTheme {
        AddPharmacyScreen(
            name = "",
            address = "",
            onNameChange = {},
            onAddressChange = {},
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}