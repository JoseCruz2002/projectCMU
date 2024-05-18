package pt.ulisboa.tecnico.cmov.frontend.ui.login_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.PharmacISTTheme

@Composable
fun LoginRoute(
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(
        username = uiState.username,
        onUsernameChange = { viewModel.updateUsername(it) },
        onLoginClicked = { viewModel.login(onLoginClicked) },
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.height(200.dp),
            style = MaterialTheme.typography.displayLarge
        )
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            label = { Text(stringResource(R.string.username)) },
            singleLine = true,
        )
        Button(
            onClick = onLoginClicked,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
        ) {
            Text(stringResource(R.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PharmacISTTheme {
        LoginScreen(
            username = "",
            onUsernameChange = {},
            onLoginClicked = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}