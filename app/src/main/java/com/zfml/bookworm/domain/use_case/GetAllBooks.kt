package com.zfml.bookworm.domain.use_case

import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.repository.BooksResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class GetAllBooks (
    private val bookRepository: BookRepository
) {
     operator fun invoke(): Flow<BooksResponse> {
       return bookRepository.getBooksFromFireStore()
    }
}