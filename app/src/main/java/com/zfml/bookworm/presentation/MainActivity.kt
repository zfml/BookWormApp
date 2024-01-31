package com.zfml.bookworm.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zfml.bookworm.navigation.Screen
import com.zfml.bookworm.navigation.SetUpNavGraph
import com.zfml.bookworm.presentation.sign_up.SignUpScreen
import com.zfml.bookworm.ui.theme.BookWormTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel> ()

    private var openSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            openSplash
        }
        setContent {
            LaunchedEffect(key1 = viewModel.getAuthState()) {
                openSplash = false
            }
            BookWormTheme {
                navController = rememberNavController()
                SetUpNavGraph(
                    navController = navController
                )
                AuthState()
            }
        }


    }
    @Composable
    fun AuthState() {
        val isUserSignOut = viewModel.getAuthState().collectAsState().value

        if(isUserSignOut) {
            NavigateToSignInScreen()
        } else {
            NavigateToHomeScreen()
        }
    }
    @Composable
    fun NavigateToSignInScreen() = navController.navigate(Screen.SignInScreen.route) {
        popUpTo(navController.graph.id){
            inclusive = true
        }
    }

    @Composable
    fun NavigateToHomeScreen() = navController.navigate(Screen.HomeScreen.route) {
        popUpTo(navController.graph.id){
            inclusive = true
        }
    }

}