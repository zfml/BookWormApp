package com.zfml.bookworm.domain.use_case

data class BookUseCases(
    val getAllBooks : GetAllBooks,
    val addBook : AddBook,
    val updateBook: UpdateBook,
    val deleteBook: DeleteBook,
    val getBook: GetBook
)