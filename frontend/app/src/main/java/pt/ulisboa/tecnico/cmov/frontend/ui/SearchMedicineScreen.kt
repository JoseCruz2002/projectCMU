package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.FrontendTheme

@Composable
fun SearchMedicineScreen(
    modifier: Modifier = Modifier
) {

}

@Preview(showBackground = true)
@Composable
fun SearchMedicineScreenPreview() {
    FrontendTheme {
        SearchMedicineScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}