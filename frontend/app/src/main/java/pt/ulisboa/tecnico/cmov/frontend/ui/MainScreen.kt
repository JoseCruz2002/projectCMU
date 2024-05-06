package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.maps.android.compose.GoogleMap
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun MainScreenRoute(
    modifier: Modifier = Modifier
) {
    MainScreen(
        modifier = modifier
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize()
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PharmacISTTheme {
        MainScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}