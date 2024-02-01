package com.zfml.bookworm.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zfml.bookworm.domain.model.Book
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookItem(
    book: Book,
    onClicked:(bookId: String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                onClicked(book.id)
            },
        tonalElevation = 1.dp
    ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
       ) {
           AsyncImage(
               modifier = Modifier
                   .height(194.dp)
                   .weight(3f),
               model = book.bookCover,
               contentDescription = book.name,
               contentScale = ContentScale.Crop
           )
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .weight(7f)
                   .height(194.dp)
                   .padding(8.dp),
           ){
               Text(
                   text = book.name,
                   fontSize = MaterialTheme.typography.titleLarge.fontSize,
               )
               Spacer(modifier = Modifier.height(8.dp))
               Text(
                   text = book.author,
                   fontSize = MaterialTheme.typography.titleMedium.fontSize,
               )
               Spacer(modifier = Modifier.height(8.dp))
               Text(
                   text = Date(book.dateBought).toFormattedString(),
                   fontSize = MaterialTheme.typography.titleMedium.fontSize,
               )



           }
       }
    }
}



fun Date.toFormattedString(): String {
    val simpleDateFormat = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}