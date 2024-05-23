package pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen

import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
fun AddPharmacyRoute(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPharmacyViewModel = viewModel(factory = AddPharmacyViewModel.Factory)
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

    val apiKey: String
    val coroutineScope = rememberCoroutineScope()

    context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).apply {
        apiKey = metaData.getString("com.google.android.geo.API_KEY").toString()
    }

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

    AddPharmacyScreen(
        name = uiState.name,
        address = uiState.address,
        onNameChange = { viewModel.updateName(it) },
        onAddressChange = { viewModel.updateAddress(it) },
        onAddPhoto = {
            requestCameraPermission(
                context,
                requestPermissionLauncher
            ) { cameraLauncher.launch(tmpUri) }
        },
        onCancel = { onCancel() },
        onConfirm = {
            onConfirm()
            Log.d("addPharmacy", "pharmacy is to be added")
            viewModel.addPharmacy(apiKey, coroutineScope)
        },
        modifier = modifier
    )
}

@Composable
fun AddPharmacyScreen(
    name: String,
    address: String,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onAddPhoto: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
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

private fun requestCameraPermission(
    context: Context,
    launcher: ActivityResultLauncher<String>,
    onGranted: () -> Unit
) =
    when {
        ContextCompat.checkSelfPermission(
            context,
            CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            onGranted()
        }

        else -> {
            // Request permission if not already granted
            launcher.launch(CAMERA)
        }
    }

@Preview(showBackground = true, locale = "en")
@Composable
fun AddPharmacyScreenPreview() {
    PharmacISTTheme {
        AddPharmacyScreen(
            name = "",
            address = "",
            onNameChange = {},
            onAddressChange = {},
            onAddPhoto = {},
            onCancel = {},
            onConfirm = {},
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}