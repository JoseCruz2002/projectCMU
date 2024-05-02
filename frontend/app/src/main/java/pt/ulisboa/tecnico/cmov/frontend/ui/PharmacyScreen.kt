package pt.ulisboa.tecnico.cmov.frontend.ui

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.components.Action
import pt.ulisboa.tecnico.cmov.frontend.ui.components.ActionRow
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun PharmacyRoute(
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    PharmacyScreen(
        name = "",
        favorite = true,
        medicines = listOf(),
        modifier
    )
}

@Composable
fun PharmacyScreen(
    name: String,
    favorite: Boolean,
    medicines: List<Medicine>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
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
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {

                }
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {

                }
            }
        }
        ActionRow(
            actions = actions,
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
            IconButton(onClick = { /*TODO*/ }) {
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
                    Text(medicine.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x" + medicine.quantity.toString(),
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

private val actions = listOf(
    Action(
        icon = Icons.Default.Directions,
        label = "Directions",
        onClick = {}
    )
)

data class Medicine(
    val name: String,
    val quantity: Int,
)

@Preview(showBackground = true)
@Composable
fun PharmacyScreenPreview() {
    val medicines = listOf(
        Medicine("benuron", 5),
        Medicine("benurin", 5),
        Medicine("benurum", 5)
    )

    PharmacISTTheme {
        PharmacyScreen(
            "My Pharmacy",
            true,
            medicines = medicines,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}