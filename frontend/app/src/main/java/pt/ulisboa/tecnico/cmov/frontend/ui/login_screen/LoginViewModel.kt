package pt.ulisboa.tecnico.cmov.frontend.ui.login_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun updateUsername(updatedUsername: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = updatedUsername
            )
        }
    }

    fun login(onLoginClicked: () -> Unit) {
        onLoginClicked()
    }
}