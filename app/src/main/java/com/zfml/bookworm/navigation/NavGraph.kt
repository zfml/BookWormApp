package com.zfml.bookworm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zfml.bookworm.core.Constants
import com.zfml.bookworm.core.Constants.ADD_BOOK_SCREEN_ARG_KEY
import com.zfml.bookworm.presentation.addBook.AddBookEvent
import com.zfml.bookworm.presentation.addBook.AddBookScreen
import com.zfml.bookworm.presentation.addBook.AddBookViewModel
import com.zfml.bookworm.presentation.home.HomeScreen
import com.zfml.bookworm.presentation.home.HomeViewModel
import com.zfml.bookworm.presentation.sign_in.SignInScreen
import com.zfml.bookworm.presentation.sign_in.SignInViewModel
import com.zfml.bookworm.presentation.sign_up.SignUpScreen
import com.zfml.bookworm.presentation.sign_up.SignUpViewModel

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
                navController.navigateUp()
            }
        )

    }
}
fun NavGraphBuilder.signUpRoute(
    navigateToSignIn:() -> Unit
) {
    composable(Screen.SignUpScreen.route) {

        val viewModel: SignUpViewModel = hiltViewModel()
        val signUpUiState by viewModel.signUpUiState.collectAsStateWithLifecycle()

        SignUpScreen(
            signUpUiState = signUpUiState,
            signUpWithEmailAndPassword = { email, password ->
                 viewModel.signUpWithEmailAndPassword(email = email, password = password)
            },
            navigateToSignIn = navigateToSignIn
        )
    }
}

fun NavGraphBuilder.signInRoute(
    navigateToSignUp:() -> Unit
) {
    composable(Screen.SignInScreen.route) {

        val viewModel: SignInViewModel = hiltViewModel()
        val signInUiState by viewModel.signInUiState.collectAsStateWithLifecycle()
        SignInScreen(
            signInUiState = signInUiState,
            signInWithEmailAndPassword = { email,password ->
                  viewModel.signInWithEmailAndPassword(email = email, password = password)
            } ,
            navigateToSignUp = navigateToSignUp
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToHomeScreen: () -> Unit,
    navigateToAddBookScreenArg: (String) -> Unit
) {
    composable(Screen.HomeScreen.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val booksUiState by viewModel.booksUiState.collectAsStateWithLifecycle()

        HomeScreen(
            navigateToAddBookScreen = navigateToHomeScreen,
            navigateToAddBookScreenWithArg = navigateToAddBookScreenArg,
            booksUiState = booksUiState,
            signOut = { viewModel.signOut()}
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
        val viewModel: AddBookViewModel = hiltViewModel()
        val addBookUiState by viewModel.addBookUiState.collectAsStateWithLifecycle()

        AddBookScreen(
            addBookUiState = addBookUiState ,
            currentBookId = viewModel.currentBookId,
            onBookNameChanged = {
                   viewModel.onEvent(AddBookEvent.BookNameChange(it))
            },
            onAuthorNameChanged = {
                   viewModel.onEvent(AddBookEvent.AuthorNameChange(it))
            },
            onBoughtDateChanged = {
                   viewModel.onEvent(AddBookEvent.BoughtDateChange(it))
            },
            onSelectedImageChanged = {
                   viewModel.onEvent(AddBookEvent.ImageChange(it))
            },
            saveBook = {
                    viewModel.onEvent(AddBookEvent.Save)
            },
            deleteBook = {
                    viewModel.onEvent(AddBookEvent.DeleteBook)
            },
            navigateHomeScreen = navigateToHomeScreen
        )
    }
}