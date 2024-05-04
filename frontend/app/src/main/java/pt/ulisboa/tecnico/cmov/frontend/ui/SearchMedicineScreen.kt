package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchMedicineRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchMedicineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchMedicineScreen(
        query = uiState.query,
        results = listOf(),
        onQueryChange = { viewModel.updateQuery(it) },
        onSearch = {},
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMedicineScreen(
    query: String,
    results: List<String>,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = true,
            onActiveChange = {},
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) }
        ) {
            results.forEach { result ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        result,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMedicineScreenPreview() {
    PharmacISTTheme {
        SearchMedicineScreen(
            query = "",
            onQueryChange = {},
            onSearch = {},
            results = listOf("result1", "other result"),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}