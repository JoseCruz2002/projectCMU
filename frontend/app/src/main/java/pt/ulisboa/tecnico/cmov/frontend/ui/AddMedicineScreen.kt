package pt.ulisboa.tecnico.cmov.frontend.ui

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
    AddMedicineScreen(
        name = "",
        description = "",
        quantity = "",
        onNameChange = {},
        onDescriptionChange = {},
        onQuantityChange = {},
        modifier = modifier
    )
}

@Composable
fun AddMedicineScreen(
    name: String,
    description: String,
    quantity: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
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
                    value = quantity,
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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Remove, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Add, contentDescription = null)
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
            quantity = "",
            onNameChange = {},
            onDescriptionChange = {},
            onQuantityChange = {},
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}