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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zfml.bookworm.component.Loading

@Composable
fun SignUpScreen(
    signUpUiState: SignUpUiState,
    signUpWithEmailAndPassword:(email:String,password: String) -> Unit,
    navigateToSignIn: () -> Unit
){


    val context  = LocalContext.current
    LaunchedEffect(key1 = signUpUiState.errorMessage) {
        if(signUpUiState.errorMessage != "") {
            Toast.makeText(
                context,
                signUpUiState.errorMessage,
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
                signUpWithEmailAndPassword(email, password)
            },
            navigateToSignIn = navigateToSignIn
        )
        if(signUpUiState.isLoading) {
            Loading()
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        signUpUiState = SignUpUiState(isSuccess = false, isLoading = true),
        signUpWithEmailAndPassword = { email, password ->

        },
        navigateToSignIn = {}
    )
}