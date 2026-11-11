package com.tecsup.fragancias.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.tecsup.fragancias.ui.detalle.DetalleScreen
import com.tecsup.fragancias.ui.detalle.DetalleViewModel
import com.tecsup.fragancias.ui.favoritos.FavoritosScreen
import com.tecsup.fragancias.ui.favoritos.FavoritosViewModel
import com.tecsup.fragancias.ui.lista.ListaScreen
import com.tecsup.fragancias.ui.lista.ListaViewModel
import com.tecsup.fragancias.ui.login.LoginScreen
import com.tecsup.fragancias.ui.login.LoginViewModel
import com.tecsup.fragancias.ui.login.PerfilScreen
import com.tecsup.fragancias.ui.login.RegistroScreen
import com.tecsup.fragancias.ui.login.RegistroViewModel
import com.tecsup.fragancias.ui.login.SplashScreen

@Composable
fun AppNav(appContainer: AppContainer) {
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.factory(appContainer)
    )
    val estadoLogin by loginViewModel.estado.collectAsStateWithLifecycle()
    val verificando by loginViewModel.verificando.collectAsStateWithLifecycle()
    val splashListo by loginViewModel.splashListo.collectAsStateWithLifecycle()
    val claveSesion by loginViewModel.claveSesion.collectAsStateWithLifecycle()

    if (!splashListo || verificando) {
        SplashScreen()
        return
    }

    val sesion = (estadoLogin as? UiState.Exito)?.datos
    if (sesion == null) {
        val authNavController = rememberNavController()
        NavHost(
            navController = authNavController,
            startDestination = "login",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(280)
                ) + fadeIn(tween(280))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(240)
                ) + fadeOut(tween(240))
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(280)
                ) + fadeIn(tween(280))
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(240)
                ) + fadeOut(tween(240))
            }
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = loginViewModel,
                    onIrARegistro = {
                        authNavController.navigate("registro")
                    }
                )
            }
            composable("registro") {
                val registroViewModel: RegistroViewModel = viewModel(
                    factory = RegistroViewModel.factory(appContainer)
                )
                RegistroScreen(
                    viewModel = registroViewModel,
                    onVolver = { authNavController.popBackStack() },
                    onRegistroExitoso = { correoRegistrado ->
                        loginViewModel.prepararCorreo(correoRegistrado)
                        authNavController.popBackStack()
                    }
                )
            }
        }
        return
    }

    val esAdmin = sesion.rol == Rol.ADMIN
    val navController = rememberNavController()

    fun navegarTab(destino: String) {
        if (destino == "admin" && !esAdmin) return
        navController.navigate(destino) {
            popUpTo("lista") {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = "lista",
        enterTransition = { fadeIn(tween(220)) },
        exitTransition = { fadeOut(tween(180)) }
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
                onNavegarTab = ::navegarTab
            )
        }
        composable("favoritos") {
            val favoritosViewModel: FavoritosViewModel = viewModel(
                factory = FavoritosViewModel.factory(appContainer)
            )
            FavoritosScreen(
                viewModel = favoritosViewModel,
                esAdmin = esAdmin,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onNavegarTab = ::navegarTab
            )
        }
        composable("perfil") {
            PerfilScreen(
                sesion = sesion,
                claveSesion = claveSesion,
                onNavegarTab = ::navegarTab,
                onCerrarSesion = loginViewModel::cerrarSesion
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
                    onNavegarTab = ::navegarTab
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
        composable(
            route = "detalle/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                ) + fadeIn(tween(300))
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(220))
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(280)
                ) + fadeOut(tween(280))
            }
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
