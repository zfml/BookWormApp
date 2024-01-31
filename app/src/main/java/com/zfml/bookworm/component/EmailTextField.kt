package com.zfml.bookworm.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
        ,
        value = email,
        onValueChange = onEmailChanged,
        placeholder = {
            Text(text = "Email")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}
@Preview
@Composable
fun EmailTextFieldPreview() {
    EmailTextField(email = "", onEmailChanged ={} )
}
