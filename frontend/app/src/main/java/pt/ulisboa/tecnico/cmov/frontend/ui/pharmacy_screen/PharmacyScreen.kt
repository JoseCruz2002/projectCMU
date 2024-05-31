package pt.ulisboa.tecnico.cmov.frontend.ui.pharmacy_screen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.model.PharmacyMedicine
import pt.ulisboa.tecnico.cmov.frontend.ui.components.Action
import pt.ulisboa.tecnico.cmov.frontend.ui.components.ActionRow
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun PharmacyRoute(
    onSelectMedicine: (String) -> Unit,
    onCreateMedicine: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PharmacyViewModel = viewModel(factory = PharmacyViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val barLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result.contents != null) {
            onCreateMedicine(uiState.pharmacy.id, result.contents)
        }
    }

    PharmacyScreen(
        pharmacy = uiState.pharmacy,
        favorite = uiState.favorite,
        onFavoriteChange = { viewModel.toggleFavorite(it) },
        onScan = { scanCode(context, barLauncher) },
        onGetDirections = { getDirections(context, uiState.pharmacy) },
        onShare = { share(context, uiState.pharmacy) },
        onSelectMedicine = onSelectMedicine,
        modifier = modifier
    )
}

@Composable
fun PharmacyScreen(
    pharmacy: Pharmacy,
    favorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit,
    onScan: () -> Unit,
    onGetDirections: () -> Unit,
    onShare: () -> Unit,
    onSelectMedicine: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .displayCutoutPadding()
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pharmacy.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            IconToggleButton(
                checked = favorite,
                onCheckedChange = onFavoriteChange
            ) {
                if (favorite) {
                    Icon(Icons.Default.Star, contentDescription = null)
                } else {
                    Icon(Icons.Default.StarBorder, contentDescription = null)
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.image_height))
        ) {
            Row {
                // Placeholders for image and map
                Map(
                    pharmacy = pharmacy,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                AsyncImage(
                    model = ImageRequest
                        .Builder(context = LocalContext.current)
                        .data(
                            if (isWifiConnected(context = LocalContext.current)) {pharmacy.img} else {}
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = pharmacy.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
        ActionRow(
            actions = listOf(
                Action(
                    icon = Icons.Default.Directions,
                    label = stringResource(R.string.directions),
                    onClick = onGetDirections
                ),
                Action(
                    icon = Icons.Default.Share,
                    label = stringResource(R.string.share),
                    onClick = onShare
                )
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.pharmacy_medicine_list),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onScan) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
            }
        }
        Column {
            pharmacy.medicines.forEach { medicine ->
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(medicine.medicine.name) },
                    modifier = Modifier.clickable { onSelectMedicine(medicine.medicine.id) },
                    trailingContent = { Text(medicine.quantity.toString()) }
                )
            }
        }
    }
}

fun scanCode(
    context: Context,
    launcher: ActivityResultLauncher<ScanOptions>
) {
    val options = ScanOptions().apply {
        setPrompt(context.getString(R.string.scan_bar_code))
        setBeepEnabled(true)
        setOrientationLocked(true)
        captureActivity = CaptureActivity::class.java
    }
    launcher.launch(options)
}

fun getDirections(context: Context, pharmacy: Pharmacy) {
    val mapIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("google.navigation:q=${pharmacy.latitude},${pharmacy.longitude}")
    )
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}

fun share(context: Context, pharmacy: Pharmacy) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.app_name)
        ) // Optional subject
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(
                R.string.share_Text,
                pharmacy.name,
                pharmacy.location
            )
        ) // Content to be shared
    }
    val chooserIntent = Intent.createChooser(
        shareIntent,
        context.getString(R.string.share_via)
    )
    context.startActivity(chooserIntent)
}

@Composable
private fun Map(pharmacy: Pharmacy, modifier: Modifier = Modifier) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = CameraPositionState(
            CameraPosition(
                LatLng(pharmacy.latitude, pharmacy.longitude),
                15f, 0f, 0f
            )
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
        )
    ) {
        Marker(
            contentDescription = pharmacy.name,
            state = MarkerState(LatLng(pharmacy.latitude, pharmacy.longitude)),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PharmacyScreenPreview() {
    val medicines = listOf(
        PharmacyMedicine(Medicine("", "benuron", "", "", emptyList()), 65),
        PharmacyMedicine(Medicine("", "benurin", "", "", emptyList()), 65),
        PharmacyMedicine(Medicine("", "benurum", "", "", emptyList()), 65)
    )

    PharmacISTTheme {
        PharmacyScreen(
            Pharmacy(
                id = "",
                name = "My Farmacy",
                location = "",
                latitude = 0.0,
                longitude = 0.0,
                img = "",
                medicines = medicines
            ),
            true,
            onGetDirections = {},
            onScan = {},
            onShare = {},
            onSelectMedicine = {},
            onFavoriteChange = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

fun isWifiConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        networkInfo.type == ConnectivityManager.TYPE_WIFI
    }
}