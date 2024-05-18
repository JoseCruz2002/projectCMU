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
import pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen.AddPharmacyRoute
import pt.ulisboa.tecnico.cmov.frontend.ui.login_screen.LoginRoute
import pt.ulisboa.tecnico.cmov.frontend.ui.main_screen.MainScreenRoute
import pt.ulisboa.tecnico.cmov.frontend.ui.pharmacy_screen.PharmacyRoute
import pt.ulisboa.tecnico.cmov.frontend.ui.search_medicine_screen.SearchMedicineRoute

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
        backStackEntry?.destination?.route?.substringBefore("/") ?: PharmacISTScreen.Login.name
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
                LoginRoute(
                    onLoginClicked = { navController.navigate(PharmacISTScreen.Main.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.Main.name) {
                MainScreenRoute(
                    onPharmacyClick = { pharmacyId ->
                        navController.navigate("${PharmacISTScreen.Pharmacy.name}/$pharmacyId")
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.SearchMedicine.name) {
                SearchMedicineRoute(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PharmacISTScreen.AddPharmacy.name) {
                AddPharmacyRoute(
                    onCancel = { navController.navigate(PharmacISTScreen.Main.name) },
                    onConfirm = { navController.navigate(PharmacISTScreen.Main.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = "${PharmacISTScreen.Pharmacy.name}/{pharmacyId}") { backStackEntry ->
                PharmacyRoute(
                    pharmacyId = backStackEntry.arguments?.getString("pharmacyId")!!,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
