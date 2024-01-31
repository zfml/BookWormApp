package com.zfml.bookworm.presentation.addBook

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zfml.bookworm.component.DisplayAlertDialog
import com.zfml.bookworm.component.ProgressBar
import com.zfml.bookworm.presentation.addBook.components.BoughtDateTextField
import com.zfml.bookworm.presentation.addBook.components.UploadImageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    viewModel: AddBookViewModel = hiltViewModel(),
    navigateHomeScreen:() -> Unit
) {

    val addBookUiState by viewModel.addBookUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    var openDeleteDialog by remember{ mutableStateOf(false) }

    Scaffold(
        topBar = {
          TopAppBar(
              title = {
              Text(text = "Add Book") },
              navigationIcon = {
                   IconButton(
                       onClick = { navigateHomeScreen()},
                       enabled = !addBookUiState.isLoading
                   ) {
                          Icon(
                              imageVector = Icons.Default.ArrowBack,
                              contentDescription = "Back Icon"
                          )
                      }

              },
              actions = {
                  if(viewModel.currentBookId != "") {
                      IconButton(
                          onClick = { openDeleteDialog = true },
                          enabled = !addBookUiState.isLoading
                      ) {
                          Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
                      }
                  }
              }
          )
        },
        content = {padding ->

            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CustomTextField(
                        placeholder = "Book Name",
                        text = addBookUiState.bookName,
                        onTextChanged = {
                            viewModel.onEvent(AddBookEvent.BookNameChange(it))
                        },
                        isLoading = addBookUiState.isLoading
                    )
                    CustomTextField(
                        placeholder = "Author Name",
                        text = addBookUiState.authorName,
                        onTextChanged = {
                            viewModel.onEvent(AddBookEvent.AuthorNameChange(it))
                        },
                        isLoading = addBookUiState.isLoading
                    )

                    BoughtDateTextField(
                        boughtDate = {viewModel.onEvent(AddBookEvent.BoughtDateChange(it))},
                        isLoading = addBookUiState.isLoading
                    )

                        UploadImageContent(
                            imageUri = addBookUiState.imageUri,
                            onSelectedImage = {
                                viewModel.onEvent(AddBookEvent.ImageChange(it.toString()))
                            },
                            isLoading = addBookUiState.isLoading
                        )



                        SaveButton(
                            onClick = {
                                handleSave(
                                    bookName = addBookUiState.bookName,
                                    authorName = addBookUiState.authorName,
                                    boughtDate = addBookUiState.boughtDate,
                                    imageUri = addBookUiState.imageUri,
                                    isLoading = addBookUiState.isLoading,
                                    onInvalid = {
                                        Toast.makeText(
                                            context,
                                            it,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onSave = {
                                        viewModel.onEvent(AddBookEvent.Save)
                                    }
                                )

                            },
                            isLoading = addBookUiState.isLoading
                        )

                    if(addBookUiState.isSuccessful) {
                        navigateHomeScreen()
                    }
                }
                if(addBookUiState.isLoading) {
                    ProgressBar()
                }
            }



        }
    )

    if(openDeleteDialog) {
        DisplayAlertDialog(
            title = "Delete Book?",
            description = "Are you sure you want to delete ?",
            openDialog = openDeleteDialog,
            onClosedDialog = { openDeleteDialog = false },
            onConfirmClicked = {
                viewModel.onEvent(AddBookEvent.DeleteNote)
                openDeleteDialog = false

              },
            onDismissClicked = {openDeleteDialog = false}
        )
    }





}
fun handleSave(
    bookName: String,
    authorName: String,
    boughtDate: Long,
    imageUri: String,
    isLoading: Boolean,
    onInvalid: (String) -> Unit,
    onSave: () -> Unit
) {
    if(bookName.isEmpty()) {
        onInvalid("Book Name is Empty")
        return
    }

    if(authorName.isEmpty()) {
        onInvalid("Author Name is Empty")
        return
    }

    if(boughtDate < 1){
        onInvalid("Invalid Date")
        return
    }

    if(imageUri.isEmpty()) {
        onInvalid("Choose Some Photo")
        return
    }

       if(!isLoading) {
           onSave()
       }

}
@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick:() -> Unit,
    isLoading: Boolean,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
        ,
        onClick = onClick,
        enabled = !isLoading
    ) {
        Text(text = "Save")
    }
}

@Composable
fun CustomTextField(
    placeholder: String,
    text: String,
    onTextChanged: (String) -> Unit,
    isLoading: Boolean
) {

    Text(
        text = placeholder,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.SemiBold
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        placeholder = {
            Text(text = placeholder)
        },
        enabled = !isLoading
    )
}
