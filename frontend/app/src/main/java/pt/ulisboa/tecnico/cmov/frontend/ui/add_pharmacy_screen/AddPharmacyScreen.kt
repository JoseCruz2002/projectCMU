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
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPharmacyViewModel
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

    context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        .apply {
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
        inTheBoxFunction = { Map(onMapClick, viewModel, modifier) },
        onMapClick = {
            Log.d("addPharmacy", "map clicked on")
            onMapClick()
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
    inTheBoxFunction: @Composable () -> Unit,
    onMapClick: () -> Unit,
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
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.map_height))
                    /*.clickable { onMapClick() }*/
            ) {
                inTheBoxFunction()
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

@Composable
private fun Map(
    onMapClick: () -> Unit,
    viewModel: AddPharmacyViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()

    /*val context: Context = LocalContext.current
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            viewModel.getMyLocation(context)
        }
    }*/

    GoogleMap(
        modifier = modifier,
        cameraPositionState = CameraPositionState(
            if (uiState.value.userChoseLocation) {
                CameraPosition(uiState.value.chosenLocation, 8f, 0f, 0f)
            } else {
                /*CameraPosition(uiState.value.currentLocation, 5f, 0f, 0f)*/
                CameraPosition(LatLng(38.736946, -9.142685), 8f, 0f, 0f)
            }
        ),
        uiSettings = MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false
        ),
        properties = MapProperties(
            isMyLocationEnabled = true
        ),
        onMapClick = {
            onMapClick()
        }
    ) {
        if (uiState.value.userChoseLocation) {
            Marker(
                state = MarkerState(viewModel.uiState.collectAsState().value.chosenLocation)
            )
        }
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
            inTheBoxFunction = {},
            onMapClick = {},
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}