package com.zfml.bookworm.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zfml.bookworm.component.EmailTextField
import com.zfml.bookworm.component.PasswordTextField

@Composable
fun SignInContent(
    onSignIn: (email: String, password: String) -> Unit,
    navigateToSignUp: () -> Unit
) {


    val context = LocalContext.current
    var email by rememberSaveable{ mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        EmailTextField(
            email = email,
            onEmailChanged = {
                email = it
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            password = password,
            onPasswordChanged = {
                password = it
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            onClick = {
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    context,
                    "Field is Empty!",
                    Toast.LENGTH_SHORT
                ).show()
                return@Button
            }
            onSignIn(email, password)
        }
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .clickable {
                   navigateToSignUp()
                }
            ,
            text = buildAnnotatedString {
                val linkStyle = SpanStyle(
                 color = Color(0xFF3D3D91),
                 fontWeight = FontWeight.Bold
                 )
                append("No Account?")
                pushStyle(linkStyle)
                append(" Sign Up")
            }
        )
    }
}

@Preview
@Composable
fun SignInContentPreview() {
    SignInContent(onSignIn = { email, password ->  

    }, navigateToSignUp = {})
}