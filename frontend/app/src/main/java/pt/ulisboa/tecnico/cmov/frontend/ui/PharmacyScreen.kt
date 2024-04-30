package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.FrontendTheme

@Composable
fun PharmacyScreen(
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            name,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PharmacyScreenPreview() {
    FrontendTheme {
        PharmacyScreen(
            "Farmácia da Esquina",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}