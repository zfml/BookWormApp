package com.zfml.bookworm.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.zfml.bookworm.data.repository.AuthRepositoryImpl
import com.zfml.bookworm.data.repository.BookRepositoryImpl
import com.zfml.bookworm.domain.repository.AuthRepository
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.use_case.AddBook
import com.zfml.bookworm.domain.use_case.BookUseCases
import com.zfml.bookworm.domain.use_case.DeleteBook
import com.zfml.bookworm.domain.use_case.GetAllBooks
import com.zfml.bookworm.domain.use_case.GetBook
import com.zfml.bookworm.domain.use_case.UpdateBook
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )

    @Provides
    fun provideBookRepository(): BookRepository = BookRepositoryImpl(
        db = Firebase.firestore,
        auth = Firebase.auth,
        storage = Firebase.storage
    )

    @Provides
    @Singleton
    fun provideBookUseCase(bookRepository: BookRepository): BookUseCases = BookUseCases(
        getAllBooks = GetAllBooks(bookRepository),
        addBook = AddBook(bookRepository),
        updateBook = UpdateBook(bookRepository),
        deleteBook = DeleteBook(bookRepository),
        getBook = GetBook(bookRepository)
    )

}