package com.zfml.bookworm.domain.use_case

import androidx.core.net.toUri
import com.zfml.bookworm.domain.model.Book
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.AddBookToFireStoreResponse
import com.zfml.bookworm.domain.repository.BookRepository
import kotlinx.coroutines.flow.flow

class AddBook (
    private val bookRepository: BookRepository
) {
     suspend operator fun invoke(imageUri: String, book: Book): AddBookToFireStoreResponse  = flow {


          bookRepository.getImageUrlFromFireStorage(imageUri = imageUri.toUri())
              .collect{response ->
                  when(response) {
                      is Response.Failure -> {
                          emit(Response.Failure(response.e))
                      }
                      Response.Loading -> {
                          emit(Response.Loading)
                      }
                      is Response.Success -> {
                          bookRepository.addBookToFireStore(book = book.copy(bookCover = response.data.toString()))
                              .collect{ response ->
                                  when(response) {
                                      is Response.Failure -> {
                                          emit(Response.Failure(e = response.e))
                                      }
                                      Response.Loading -> {
                                          emit(Response.Loading)
                                      }
                                      is Response.Success -> {
                                          emit(Response.Success(true))
                                      }
                                  }

                              }
                      }
                  }
              }

    }
}