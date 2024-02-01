package com.zfml.bookworm.presentation.addBook

sealed class AddBookEvent {
    data class BookNameChange(val bookName: String): AddBookEvent()
    data class AuthorNameChange(val authorName: String): AddBookEvent()
    data class BoughtDateChange(val date: Long): AddBookEvent()
    data class ImageChange(val imageUri: String): AddBookEvent()
    object Save: AddBookEvent()
    object DeleteBook: AddBookEvent()
}