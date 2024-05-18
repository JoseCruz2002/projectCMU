package pt.ulisboa.tecnico.cmov.frontend.ui.add_medicine_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddMedicineRoute(
    modifier: Modifier = Modifier,
    viewModel: AddMedicineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AddMedicineScreen(
        name = uiState.name,
        description = uiState.description,
        quantity = uiState.quantity,
        onNameChange = { viewModel.updateName(it) },
        onDescriptionChange = { viewModel.updateDescription(it) },
        onQuantityChange = { viewModel.updateQuantity(it) },
        onIncrement = {viewModel.incrementQuantity()},
        onDecrement = {viewModel.decrementQuantity()},
        modifier = modifier
    )
}

@Composable
fun AddMedicineScreen(
    name: String,
    description: String,
    quantity: Int,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                text = stringResource(R.string.add_medicine_title),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.medicine_name))
                }
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.medicine_description))
                },
                minLines = 4
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = onQuantityChange,
                    modifier = Modifier
                        .weight(1f),
                    label = {
                        Text(stringResource(R.string.medicine_quantity))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                IconButton(onClick = onDecrement) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
                }
                IconButton(onClick = onIncrement) {
                    Icon(Icons.Default.Add, contentDescription = "Increase quantity")
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

@Preview(showBackground = true)
@Composable
fun AddMedicineScreenPreview() {
    PharmacISTTheme {
        AddMedicineScreen(
            name = "",
            description = "",
            quantity = 0,
            onNameChange = {},
            onDescriptionChange = {},
            onQuantityChange = {},
            onIncrement = {},
            onDecrement = {},
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}