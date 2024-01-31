package com.zfml.bookworm.domain.use_case

import androidx.core.net.toUri
import com.zfml.bookworm.domain.model.Book
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.repository.UpdateBookToFireStoreResponse
import kotlinx.coroutines.flow.flow

class UpdateBook (
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(currentImageUri: String, imageUri: String,book:Book): UpdateBookToFireStoreResponse = flow {
        if(currentImageUri != imageUri){
            bookRepository.deleteImage(currentImageUri)

            bookRepository.getImageUrlFromFireStorage(imageUri.toUri())
                .collect{response ->
                    when(response) {
                        is Response.Failure -> {
                            emit(Response.Failure(e = response.e))
                        }
                        Response.Loading -> {
                            emit(Response.Loading)
                        }
                        is Response.Success -> {
                            bookRepository.updateBookToFireStore(book.copy(bookCover = response.data.toString()))
                                .collect{response ->
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

        } else{
            bookRepository.updateBookToFireStore(book.copy(bookCover = imageUri))
                .collect{response ->
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