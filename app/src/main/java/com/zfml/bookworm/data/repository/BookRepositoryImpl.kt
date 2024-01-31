package com.zfml.bookworm.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.zfml.bookworm.core.Constants.COLLECTION_BOOKS
import com.zfml.bookworm.core.Constants.FIREBASE_USERS
import com.zfml.bookworm.domain.model.Book
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.AddBookToFireStoreResponse
import com.zfml.bookworm.domain.repository.AddImageUrlToFireStoreResponse
import com.zfml.bookworm.domain.repository.BookRepository
import com.zfml.bookworm.domain.repository.DeleteBookFromFireStoreResponse
import com.zfml.bookworm.domain.repository.GetBookResponse
import com.zfml.bookworm.domain.repository.GetImageUrlFromFireStorage
import com.zfml.bookworm.domain.repository.UpdateBookToFireStoreResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class BookRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
): BookRepository {


    private val bookRef = db.collection(FIREBASE_USERS)

    override fun getBooksFromFireStore() = callbackFlow {
         val userId = auth.currentUser!!.uid
        val snapShotListener = db.collection(FIREBASE_USERS)
            .document(userId)
            .collection(COLLECTION_BOOKS)
            .addSnapshotListener{ snapshot ,e ->
                Response.Loading
                val booksResponse = if(snapshot != null) {
                    val books = snapshot.toObjects(Book::class.java)
                    Response.Success(books)
                } else {
                    Response.Failure(e)
                }
                trySend(booksResponse)
            }
        awaitClose{
            snapShotListener.remove()
        }

    }
    override suspend fun getImageUrlFromFireStorage(imageUri: Uri): GetImageUrlFromFireStorage {
        return try {
            val userId = auth.currentUser!!.uid
            val remoteImagePath = "images/${userId}/${imageUri.lastPathSegment}-${System.currentTimeMillis()}"
           val downloadUrl = storage.reference.child(remoteImagePath)
               .putFile(imageUri).await()
               .storage.downloadUrl.await()
           flow { emit(Response.Success(downloadUrl)) }

        }catch (e: Exception) {
           flow { emit(Response.Failure(e)) }
        }
    }

    override suspend fun addBookToFireStore(book: Book): AddBookToFireStoreResponse {
        return try {

            val bookId = bookRef.document().id
            val userId = auth.currentUser!!.uid
            flow { emit(Response.Loading) }
            bookRef.document(userId).collection(COLLECTION_BOOKS).document(bookId)
                .set(book.copy(id = bookId))

            flow { emit(Response.Success(true)) }
        }catch (e: Exception) {
           flow { emit(Response.Failure(e)) }
        }
    }

    override suspend fun updateBookToFireStore(book: Book): UpdateBookToFireStoreResponse {
        return try {
            val userId = auth.currentUser!!.uid
            flow { emit(Response.Loading)  }

            bookRef.document(userId).collection(COLLECTION_BOOKS).document(book.id)
                .set(book)

            flow { emit(Response.Success(true)) }



        }catch (e: Exception) {
            flow { emit(Response.Failure(e))}
        }
    }

    override suspend fun deleteBookFromFireStore(bookId: String): DeleteBookFromFireStoreResponse {
       return try {
           val userId = auth.currentUser!!.uid

           flow { emit(Response.Loading) }
            bookRef.document(userId).collection(COLLECTION_BOOKS).document(bookId.trim()).delete().await()
            flow { emit(Response.Success(true)) }
        }catch (e: Exception) {
            flow { emit(Response.Failure(e)) }
        }
    }

    override suspend fun getBookById(bookId: String): GetBookResponse =
         try {
             val userId = auth.currentUser!!.uid
             val bookDocument = bookRef.document(userId).collection(COLLECTION_BOOKS).document(bookId.trim()).get().await()


             val book = bookDocument.toObject(Book::class.java) ?: Book()

             Response.Success(book)
        }catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun deleteImage(imageUri: String) {
        val userId = auth.currentUser!!.uid
        val imageName = extractImagePath(imageUri)
        storage.reference.child("images/${userId}/$imageName").delete().await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun addImageUrlToFireStore(downloadUrl: Uri): AddImageUrlToFireStoreResponse {
        TODO("Not yet implemented")
    }


    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        return chunks[2].split("?").first()
    }


}