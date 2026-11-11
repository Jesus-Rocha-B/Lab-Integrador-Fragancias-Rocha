package com.tecsup.fragancias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.domain.model.Rol
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.admin.AdminScreen
import com.tecsup.fragancias.ui.admin.AdminViewModel
import com.tecsup.fragancias.ui.components.Cargando
import com.tecsup.fragancias.ui.detalle.DetalleScreen
import com.tecsup.fragancias.ui.detalle.DetalleViewModel
import com.tecsup.fragancias.ui.favoritos.FavoritosScreen
import com.tecsup.fragancias.ui.favoritos.FavoritosViewModel
import com.tecsup.fragancias.ui.lista.ListaScreen
import com.tecsup.fragancias.ui.lista.ListaViewModel
import com.tecsup.fragancias.ui.login.LoginScreen
import com.tecsup.fragancias.ui.login.LoginViewModel

@Composable
fun AppNav(appContainer: AppContainer) {
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.factory(appContainer)
    )
    val estadoLogin by loginViewModel.estado.collectAsStateWithLifecycle()
    val verificando by loginViewModel.verificando.collectAsStateWithLifecycle()

    if (verificando) {
        Cargando()
        return
    }

    val sesion = (estadoLogin as? UiState.Exito)?.datos
    if (sesion == null) {
        LoginScreen(viewModel = loginViewModel)
        return
    }

    val esAdmin = sesion.rol == Rol.ADMIN
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
                esAdmin = esAdmin,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onFavoritosClick = {
                    navController.navigate("favoritos")
                },
                onAdminClick = {
                    if (esAdmin) {
                        navController.navigate("admin")
                    }
                },
                onCerrarSesion = loginViewModel::cerrarSesion
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
        composable("admin") {
            if (esAdmin) {
                val adminViewModel: AdminViewModel = viewModel(
                    factory = AdminViewModel.factory(appContainer)
                )
                AdminScreen(
                    viewModel = adminViewModel,
                    correoUsuario = sesion.correo,
                    onVolver = { navController.popBackStack() },
                    onCerrarSesion = loginViewModel::cerrarSesion
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
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
