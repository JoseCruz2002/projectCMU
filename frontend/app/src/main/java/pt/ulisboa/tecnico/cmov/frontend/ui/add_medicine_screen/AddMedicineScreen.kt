package pt.ulisboa.tecnico.cmov.frontend.ui.add_medicine_screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddMedicineRoute(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddMedicineViewModel = viewModel(factory = AddMedicineViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val tmpFile = File.createTempFile(
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()),
        ".jpg",
        context.cacheDir
    )

    val tmpUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tmpFile
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.updateUri(tmpUri)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(tmpUri)
        } else {
            // Permission denied, explain to the user
            Toast.makeText(
                context,
                context.getString(R.string.photo_permission_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    AddMedicineScreen(
        id = viewModel.medicineId,
        name = uiState.name,
        description = uiState.description,
        quantity = uiState.quantity,
        onNameChange = { viewModel.updateName(it) },
        onDescriptionChange = { viewModel.updateDescription(it) },
        onQuantityChange = { viewModel.updateQuantity(it) },
        onIncrement = { viewModel.incrementQuantity() },
        onDecrement = { viewModel.decrementQuantity() },
        onConfirm = {
            viewModel.addMedicine()
            onConfirm()
        },
        onCancel = onCancel,
        onAddPhoto = {
            requestCameraPermission(
                context,
                requestPermissionLauncher
            ) { cameraLauncher.launch(tmpUri) }
        },
        modifier = modifier
    )
}

@Composable
fun AddMedicineScreen(
    id: String,
    name: String,
    description: String,
    quantity: Int,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onAddPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .displayCutoutPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            Column {
                Text(
                    text = stringResource(R.string.add_medicine_title),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.medicine_id, id),
                    style = MaterialTheme.typography.labelSmall
                )
            }
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
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = onAddPhoto,
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
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }
            Button(
                onClick = onConfirm,
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
            id = "ZXR400",
            name = "",
            description = "",
            quantity = 0,
            onNameChange = {},
            onDescriptionChange = {},
            onQuantityChange = {},
            onIncrement = {},
            onDecrement = {},
            onConfirm = {},
            onCancel = {},
            onAddPhoto = {},
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}

private fun requestCameraPermission(
    context: Context,
    launcher: ActivityResultLauncher<String>,
    onGranted: () -> Unit
) =
    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            onGranted()
        }

        else -> {
            // Request permission if not already granted
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
