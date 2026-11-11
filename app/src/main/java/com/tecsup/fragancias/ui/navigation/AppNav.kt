package com.tecsup.fragancias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.ui.detalle.DetalleScreen
import com.tecsup.fragancias.ui.detalle.DetalleViewModel
import com.tecsup.fragancias.ui.favoritos.FavoritosScreen
import com.tecsup.fragancias.ui.favoritos.FavoritosViewModel
import com.tecsup.fragancias.ui.lista.ListaScreen
import com.tecsup.fragancias.ui.lista.ListaViewModel

@Composable
fun AppNav(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {
        composable("lista") {
            val listaViewModel: ListaViewModel = viewModel(
                factory = ListaViewModel.factory(appContainer)
            )
            ListaScreen(
                viewModel = listaViewModel,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onFavoritosClick = {
                    navController.navigate("favoritos")
                }
            )
        }
        composable("favoritos") {
            val favoritosViewModel: FavoritosViewModel = viewModel(
                factory = FavoritosViewModel.factory(appContainer)
            )
            FavoritosScreen(
                viewModel = favoritosViewModel,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = "detalle/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            val detalleViewModel: DetalleViewModel = viewModel(
                factory = DetalleViewModel.factory(appContainer, itemId)
            )
            DetalleScreen(
                viewModel = detalleViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
