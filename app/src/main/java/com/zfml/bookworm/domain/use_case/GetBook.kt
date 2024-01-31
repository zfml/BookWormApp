package com.zfml.bookworm.domain.use_case

import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.repository.GetBookResponse

class GetBook(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): GetBookResponse {
        return bookRepository.getBookById(bookId)
    }
}