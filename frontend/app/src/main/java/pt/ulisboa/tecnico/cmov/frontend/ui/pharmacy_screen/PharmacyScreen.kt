package pt.ulisboa.tecnico.cmov.frontend.ui.pharmacy_screen

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
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
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.components.Action
import pt.ulisboa.tecnico.cmov.frontend.ui.components.ActionRow
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun PharmacyRoute(
    modifier: Modifier = Modifier,
    viewModel: PharmacyViewModel = viewModel(factory = PharmacyViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    PharmacyScreen(
        pharmacy = uiState.pharmacy,
        favorite = true,
        medicines = listOf(),
        modifier
    )
}

@Composable
fun PharmacyScreen(
    pharmacy: Pharmacy,
    favorite: Boolean,
    medicines: List<Pair<Medicine, Long>>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val barLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if(result.contents != null) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Result")
            builder.setMessage(result.contents)
            builder.setPositiveButton("OK", DialogInterface.OnClickListener
                { dialog, _ ->
                    dialog.dismiss()
                }).show()
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
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
                onCheckedChange = { }
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
                        .data(pharmacy.img)
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
        val context = LocalContext.current
        ActionRow(
            actions = listOf(
                Action(
                    icon = Icons.Default.Directions,
                    label = stringResource(R.string.directions),
                    onClick = {
                        val mapIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=${pharmacy.latitude},${pharmacy.longitude}")
                        )
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    }
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
            IconButton(onClick = { scanCode(context, barLauncher) }) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
            }
        }
        Column {
            medicines.forEach { medicine ->
                Divider()
                Row(
                    modifier = Modifier
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(medicine.first.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x" + medicine.second.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
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
        Pair(Medicine("benuron", "", "", emptyList()), 65L),
        Pair(Medicine("benurin", "", "", emptyList()), 4L),
        Pair(Medicine("benurum", "", "", emptyList()), 13L),
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
                medicines = emptyMap()
            ),
            true,
            medicines = medicines,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}