package com.zfml.bookworm.presentation.sign_up

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zfml.bookworm.component.Loading

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToSignIn: () -> Unit
){

    val signUpUiState by viewModel.signUpUiState.collectAsStateWithLifecycle()

    val context  = LocalContext.current
    LaunchedEffect(key1 = signUpUiState.error) {
        if(signUpUiState.error != "") {
            Toast.makeText(
                context,
                signUpUiState.error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
            ,
            text = "Book Worm",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Light
            )
        )
        SignUpContent(
            onSignUp = { email, password ->
                viewModel.signUpWithEmailAndPassword(email, password)
            },
            navigateToSignIn = navigateToSignIn
        )
        if(signUpUiState.isLoading) {
            Loading()
        }
    }


}