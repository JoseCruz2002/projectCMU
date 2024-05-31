package pt.ulisboa.tecnico.cmov.frontend.ui.search_medicine_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun SearchMedicineRoute(
    onSelectMedicine: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchMedicineViewModel = viewModel(factory = SearchMedicineViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchMedicineScreen(
        query = uiState.query,
        results = uiState.results,
        onQueryChange = { viewModel.updateQuery(it) },
        onSearch = { viewModel.searchMedicine() },
        onSelectMedicine = onSelectMedicine,
        modifier = modifier,
    )
}

@Composable
fun SearchMedicineScreen(
    query: String,
    results: List<Medicine>?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSelectMedicine: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.displayCutoutPadding()) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.medicine_search_hint)) },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            singleLine = true
        )
        when {
            results == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.medicine_search_no_search))
                }
            }

            results.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.medicine_search_no_results))
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                ) {
                    results.forEach { result ->
                        ListItem(headlineContent = { Text(text = result.name) },
                            modifier = Modifier.clickable { onSelectMedicine(result.id) })
                        HorizontalDivider()
                    }
                }
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
            onSelectMedicine = {},
            results = emptyList(),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}