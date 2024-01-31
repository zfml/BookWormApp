package com.zfml.bookworm.domain.use_case

import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.repository.DeleteBookFromFireStoreResponse
import kotlinx.coroutines.flow.flow

class DeleteBook (
    private val bookRepository: BookRepository
){
    suspend operator fun invoke(imageUri: String, bookId: String): DeleteBookFromFireStoreResponse = flow{
        bookRepository.deleteImage(imageUri)
        bookRepository.deleteBookFromFireStore(bookId)
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