package com.zfml.bookworm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zfml.bookworm.core.Constants
import com.zfml.bookworm.core.Constants.ADD_BOOK_SCREEN_ARG_KEY
import com.zfml.bookworm.presentation.addBook.AddBookScreen
import com.zfml.bookworm.presentation.home.HomeScreen
import com.zfml.bookworm.presentation.sign_in.SignInScreen
import com.zfml.bookworm.presentation.sign_up.SignUpScreen

@Composable
fun SetUpNavGraph(
   navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {
        signInRoute(
            navigateToSignUp = {
                navController.navigate(Screen.SignUpScreen.route)
            }
        )

        signUpRoute(
            navigateToSignIn = {
                navController.popBackStack()
            }
        )

        homeRoute(
            navigateToHomeScreen = {
                navController.navigate(Screen.AddScreen.route)
            },
            navigateToAddBookScreenArg = {
                navController.navigate(Screen.AddScreen.passBookId(it))
            }

        )

        addBookRoute(
            navigateToHomeScreen = {
                navController.popBackStack()
            }
        )

    }
}
fun NavGraphBuilder.signUpRoute(
    navigateToSignIn:() -> Unit
) {
    composable(Screen.SignUpScreen.route) {
        SignUpScreen(
            navigateToSignIn = navigateToSignIn
        )
    }
}

fun NavGraphBuilder.signInRoute(
    navigateToSignUp:() -> Unit
) {
    composable(Screen.SignInScreen.route) {
        SignInScreen(
            navigateToSignUp = navigateToSignUp
        )


    }
}

fun NavGraphBuilder.homeRoute(
    navigateToHomeScreen: () -> Unit,
    navigateToAddBookScreenArg: (String) -> Unit
) {
    composable(Screen.HomeScreen.route) {
        HomeScreen(
            navigateToAddBookScreen = navigateToHomeScreen,
            navigateToAddBookScreenWithArg = navigateToAddBookScreenArg
        )
    }
}

fun NavGraphBuilder.addBookRoute(
    navigateToHomeScreen:() -> Unit
) {
    composable(
        Screen.AddScreen.route,
        arguments = listOf(
            navArgument(name = ADD_BOOK_SCREEN_ARG_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        AddBookScreen(
            navigateHomeScreen = navigateToHomeScreen
        )
    }
}