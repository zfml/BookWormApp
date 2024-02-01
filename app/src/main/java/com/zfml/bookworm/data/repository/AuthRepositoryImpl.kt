package com.zfml.bookworm.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser
    override val displayName = auth.currentUser?.displayName.toString()
    override val photoUrl = auth.currentUser?.photoUrl.toString()

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
    ) = try {
        auth.createUserWithEmailAndPassword(email,password).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ) = try {
        auth.signInWithEmailAndPassword(email,password).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener{auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),auth.currentUser == null)

    override suspend fun signOut() = auth.signOut()
}