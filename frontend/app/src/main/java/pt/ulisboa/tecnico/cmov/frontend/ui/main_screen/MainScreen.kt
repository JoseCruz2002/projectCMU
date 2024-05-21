package pt.ulisboa.tecnico.cmov.frontend.ui.main_screen

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.ui.components.CreateMap
import pt.ulisboa.tecnico.cmov.frontend.ui.components.getLatLngFromPlace

@Composable
fun MainScreenRoute(
    onPharmacyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val apiKey: String
    val coroutineScope = rememberCoroutineScope()

    context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).apply {
        apiKey = metaData.getString("com.google.android.geo.API_KEY").toString()
    }

    MainScreen(
        viewModel = viewModel,
        pharmacies = uiState.pharmacies,
        onPharmacyClick = onPharmacyClick,
        query = uiState.query,
        results = listOf(),
        onQueryChange = { viewModel.updateQuery(it) },
        onActiveChange = { viewModel.updateActive(it) },
        onSearch = { query ->
            coroutineScope.launch {
                getLatLngFromPlace(viewModel, uiState.query, apiKey)
            }
        },
        active = uiState.active,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    pharmacies: List<Pharmacy>,
    onPharmacyClick: (String) -> Unit,
    query: String,
    results: List<String>,
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    CreateMap(
        viewModel = viewModel,
        modifier = modifier,
        onPharmacyClick = onPharmacyClick
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                .padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
                .fillMaxWidth(),
            windowInsets = WindowInsets(0, 0, 0, 0),
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
            viewModel = MainScreenViewModel()
            query = "Search location...",
            onQueryChange = {},
            onActiveChange = {},
            onSearch = {},
            results = listOf("result1", "other result"),
            active = false,
            pharmacies = listOf(
                Pharmacy("", "myPharmacy", "Lisbon", 0.0, 0.0, "", mapOf()),
                Pharmacy("", "Other Pharmacy", "Lisbon", 10.0, 10.0, "", mapOf()),
            ),
            onPharmacyClick = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}*/