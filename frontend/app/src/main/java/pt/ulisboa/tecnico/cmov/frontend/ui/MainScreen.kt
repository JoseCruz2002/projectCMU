package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.components.MapComposeAPI

@Composable
fun MainScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    MainScreen(
        pharmacies = uiState.pharmacies,
        query = uiState.query,
        results = listOf(),
        onQueryChange = { viewModel.updateQuery(it) },
        onActiveChange = { viewModel.updateActive(it) },
        onSearch = { TODO("Not yet implemented") },
        active = uiState.active,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    pharmacies: List<Pharmacy>,
    query: String,
    results: List<String>,
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    MapComposeAPI().InitiateMap()

    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onActiveChange = onActiveChange,
            onSearch = onSearch,
            active = active,
            placeholder = {
                Text(
                    stringResource(id = R.string.search_location)
                )
            },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
            /*.offset(x = 0.dp, y = -50.dp)*/,
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

/*@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PharmacISTTheme {
        MainScreen(
            query = "Search location...",
            onQueryChange = {},
            onActiveChange = {},
            onSearch = {},
            results = listOf("result1", "other result"),
            active = false,
            pharmacies = listOf(
                Pharmacy("myPharmacy", "Lisbon", "", mapOf()),
                Pharmacy("Other Pharmacy", "Lisbon", "", mapOf()),
            ),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}*/