package com.zfml.bookworm.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.bookworm.domain.model.Book
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.repository.BooksResponse
import com.zfml.bookworm.domain.use_case.BookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val bookUseCases: BookUseCases,
): ViewModel() {

    private val _booksUiState = MutableStateFlow(BooksUiState())
    val booksUiState = _booksUiState.asStateFlow()


    fun signOut() {
        viewModelScope.launch {
            bookRepository.signOut()
        }
    }


    init {
        getAllBooks()
    }

    private fun getAllBooks() = viewModelScope.launch {
        _booksUiState.update {
            it.copy(
                isLoading = true
            )
        }
        bookUseCases.getAllBooks.invoke().collect{response ->
            when(response) {
                is Response.Failure -> {
                    _booksUiState.update {
                        it.copy(
                            errorMessage = response.e.toString()
                        )
                    }
                }
                Response.Loading -> {
                    _booksUiState.update {
                        it.copy(isLoading = true)
                    }
                }
                is Response.Success -> {
                    _booksUiState.update {
                        it.copy(
                            books = response.data,
                            isLoading = false
                        )
                    }
                }
            }

        }
    }
}

data class BooksUiState(
    val books : List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
)