package com.zfml.bookworm.domain.repository

import android.net.Uri
import com.zfml.bookworm.domain.model.Book
import com.zfml.bookworm.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias Books = List<Book>
typealias BooksResponse = Response<Books>

typealias GetBookResponse = Response<Book>

typealias GetImageUrlFromFireStorage = Flow<Response<Uri>>
typealias AddImageUrlToFireStoreResponse = Response<Boolean>

typealias AddBookToFireStoreResponse = Flow<Response<Boolean>>
typealias UpdateBookToFireStoreResponse = Flow<Response<Boolean>>
typealias DeleteBookFromFireStoreResponse = Flow<Response<Boolean>>

interface BookRepository {

    fun getBooksFromFireStore(): Flow<BooksResponse>


    suspend fun getImageUrlFromFireStorage(imageUri: Uri): GetImageUrlFromFireStorage

    suspend fun addImageUrlToFireStore(downloadUrl: Uri): AddImageUrlToFireStoreResponse


    suspend fun addBookToFireStore(book: Book): AddBookToFireStoreResponse

    suspend fun updateBookToFireStore(book: Book): UpdateBookToFireStoreResponse

    suspend fun deleteBookFromFireStore(bookId: String): DeleteBookFromFireStoreResponse

    suspend fun getBookById(bookId: String): GetBookResponse

    suspend fun deleteImage(imageUri: String)

    suspend fun signOut()




}