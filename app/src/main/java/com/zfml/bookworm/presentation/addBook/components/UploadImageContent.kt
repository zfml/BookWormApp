package com.zfml.bookworm.presentation.addBook.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UploadImageContent(
    imageUri: String,
    onSelectedImage: (Uri) -> Unit,
    isLoading:Boolean
){

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ){ imageUri ->
        imageUri?.let {
            onSelectedImage(it)
        }
    }
    Text(
        text = "Upload Book Cover",
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.SemiBold
    )
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
        ,
        model =  imageUri,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    Button(
        onClick = {
        galleryLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    },
    enabled = !isLoading
    ) {
        Text(text = "Upload Image")
    }


}