package com.zfml.bookworm.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>

interface AuthRepository {

    val currentUser: FirebaseUser?

    val displayName: String
    val photoUrl: String

    suspend fun signUpWithEmailAndPassword(email: String, password: String): SignUpResponse


    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResponse

    suspend fun signOut()

    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>



}