package com.zfml.bookworm.presentation.addBook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.bookworm.core.Constants.ADD_BOOK_SCREEN_ARG_KEY
import com.zfml.bookworm.domain.model.Book
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.use_case.BookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val bookUseCases: BookUseCases,
) : ViewModel() {

    var currentBookId by mutableStateOf("")
        private set

    private var currentImageUri by mutableStateOf("")

    private val _addBookUiState = MutableStateFlow(AddBookUiState())
    val addBookUiState = _addBookUiState.asStateFlow()


    init {
        getBook()
    }


    private fun getBook() {

        _addBookUiState.update {
            it.copy(
                isLoading = true
            )
        }
        savedStateHandle.get<String>(ADD_BOOK_SCREEN_ARG_KEY)?.let {
            viewModelScope.launch {
                when (val getBookResponse = bookUseCases.getBook.invoke(it)) {
                    is Response.Failure -> {
                        _addBookUiState.update {
                            it.copy(
                                errorMessage = "Fail to Load Book"
                            )
                        }
                    }

                    Response.Loading -> {
                        _addBookUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Response.Success -> {

                        currentBookId = getBookResponse.data.id
                        currentImageUri = getBookResponse.data.bookCover.toString()
                        _addBookUiState.update {
                            it.copy(
                                bookName = getBookResponse.data.name,
                                authorName = getBookResponse.data.author,
                                boughtDate = getBookResponse.data.dateBought,
                                imageUri = getBookResponse.data.bookCover.toString(),
                                isLoading = false
                            )
                        }

                    }
                }
            }
        }
    }


    fun onEvent(event: AddBookEvent) = when (event) {

        is AddBookEvent.AuthorNameChange -> {
            _addBookUiState.update {
                it.copy(
                    authorName = event.authorName
                )
            }
        }

        is AddBookEvent.BookNameChange -> {
            _addBookUiState.update {
                it.copy(
                    bookName = event.bookName
                )
            }
        }

        is AddBookEvent.BoughtDateChange -> {
            _addBookUiState.update {
                it.copy(
                    boughtDate = event.date
                )
            }
        }

        AddBookEvent.Save -> {

            if (currentBookId != "") {
                updateBookToFirebaseStore()
            } else {
                addBookToFireStore()
            }

        }

        is AddBookEvent.ImageChange -> {
            _addBookUiState.update {
                it.copy(
                    imageUri = event.imageUri
                )
            }
        }

        AddBookEvent.DeleteNote -> {
            deleteBookFromFirebase()
        }
    }


    private fun deleteBookFromFirebase() {
        _addBookUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
               bookUseCases.deleteBook(imageUri = _addBookUiState.value.imageUri,currentBookId)
                .collect { response ->
                    when (response) {
                        is Response.Failure -> {
                            _addBookUiState.update {
                                it.copy(
                                    errorMessage = response.e?.message ?: ""
                                )
                            }
                        }

                        Response.Loading -> {
                            _addBookUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Response.Success -> {
                            _addBookUiState.update {
                                it.copy(
                                    isSuccessful = true,
                                    isLoading = false
                                )
                            }
                        }

                    }

                }
        }
    }

    private fun addBookToFireStore() {
        viewModelScope.launch {
            _addBookUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val book = Book(
                name = _addBookUiState.value.bookName,
                author = _addBookUiState.value.authorName,
                dateBought = _addBookUiState.value.boughtDate
            )
            bookUseCases.addBook.invoke(_addBookUiState.value.imageUri, book = book)
                .collect { response ->
                    when (response) {
                        is Response.Failure -> {
                            _addBookUiState.update {
                                it.copy(
                                    errorMessage = response.e?.message ?: ""
                                )
                            }
                        }

                        Response.Loading -> {
                            _addBookUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Response.Success -> {
                            _addBookUiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccessful = true,
                                    bookName = "",
                                    authorName = "",
                                    boughtDate = 0,
                                    imageUri = ""
                                )
                            }
                        }
                    }
                }

        }

    }

    private fun updateBookToFirebaseStore() {
        viewModelScope.launch {

            _addBookUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val book = Book(
                id = currentBookId,
                name = addBookUiState.value.bookName,
                author = addBookUiState.value.authorName,
                dateBought = addBookUiState.value.boughtDate
            )

            bookUseCases.updateBook(currentImageUri,_addBookUiState.value.imageUri,book)
                .collect { response ->
                    when (response) {
                        is Response.Failure -> {
                            _addBookUiState.update {
                                it.copy(
                                    errorMessage = response.e?.message ?: ""
                                )
                            }
                        }

                        Response.Loading -> {
                            _addBookUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Response.Success -> {
                            _addBookUiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccessful = true,
                                    bookName = "",
                                    authorName = "",
                                    boughtDate = 0,
                                    imageUri = ""
                                )
                            }
                        }
                    }

                }
        }
    }

}


data class AddBookUiState(
    val bookName: String = "",
    val authorName: String = "",
    val boughtDate: Long = 0,
    val imageUri: String = "",
    val errorMessage: String = "",
    val isInvalid: Boolean = true,
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
)