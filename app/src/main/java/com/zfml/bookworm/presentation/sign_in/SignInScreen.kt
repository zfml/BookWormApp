package com.zfml.bookworm.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.zfml.bookworm.component.Loading
import com.zfml.bookworm.domain.model.Response

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit
) {
    val context = LocalContext.current
    val signInUiState by viewModel.signInUiState.collectAsState()

    LaunchedEffect(key1 = signInUiState.error) {
        if(signInUiState.error != "") {
           Toast.makeText(
               context,
               signInUiState.error,
               Toast.LENGTH_SHORT
           ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
        ,
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

        SignInContent(
            onSignIn = { email, password ->
                viewModel.signInWithEmailAndPassword(email, password)
            },
            navigateToSignUp = navigateToSignUp
        )

        if(signInUiState.isLoading) {
            Loading()
        }







    }
}


@Preview
@Composable
fun SignInPreview() {

}