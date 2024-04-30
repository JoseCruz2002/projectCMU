package pt.ulisboa.tecnico.cmov.frontend.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ulisboa.tecnico.cmov.frontend.R
import pt.ulisboa.tecnico.cmov.frontend.ui.theme.FrontendTheme

@Composable
fun LoginScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    onLogin: () -> Unit,
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
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.username)) },
            singleLine = true,
        )
        Button(
            onClick = onLogin,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FrontendTheme {
        LoginScreen(
            username = "",
            onUsernameChange = {},
            onLogin = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}