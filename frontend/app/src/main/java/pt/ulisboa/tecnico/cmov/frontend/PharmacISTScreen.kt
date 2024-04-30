package pt.ulisboa.tecnico.cmov.frontend

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.ulisboa.tecnico.cmov.frontend.ui.AddPharmacyScreen
import pt.ulisboa.tecnico.cmov.frontend.ui.LoginScreen
import pt.ulisboa.tecnico.cmov.frontend.ui.MainScreen
import pt.ulisboa.tecnico.cmov.frontend.ui.PharmacyScreen
import pt.ulisboa.tecnico.cmov.frontend.ui.SearchMedicineScreen

enum class PharmacISTScreen {
    Login,
    Main,
    SearchMedicine,
    AddPharmacy,
    Pharmacy,
}

@Composable
fun BottomNav(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PharmacISTScreen.valueOf(
        backStackEntry?.destination?.route ?: PharmacISTScreen.Login.name
    )

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Map, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_main)) },
            selected = currentScreen == PharmacISTScreen.Main,
            onClick = {
                navController.navigate(PharmacISTScreen.Main.name)
            }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_search_medicine)) },
            selected = currentScreen == PharmacISTScreen.SearchMedicine,
            onClick = {
                navController.navigate(PharmacISTScreen.SearchMedicine.name)
            }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_add_pharmacy)) },
            selected = currentScreen == PharmacISTScreen.AddPharmacy,
            onClick = {
                navController.navigate(PharmacISTScreen.AddPharmacy.name)
            }
        )
    }
}

@Composable
fun PharmacISTApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomNav(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PharmacISTScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = PharmacISTScreen.Login.name) {
                LoginScreen(
                    username = "",
                    onUsernameChange = {},
                    onLogin = { navController.navigate(PharmacISTScreen.Main.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.Main.name) {
                MainScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.SearchMedicine.name) {
                SearchMedicineScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.AddPharmacy.name) {
                AddPharmacyScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.Pharmacy.name) {
                PharmacyScreen(
                    name = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
