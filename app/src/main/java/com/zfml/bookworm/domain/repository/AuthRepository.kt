package com.zfml.bookworm.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.zfml.bookworm.domain.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun signUpWithEmailAndPassword(email: String, password: String): SignUpResponse


    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResponse

    suspend fun signOut()

    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>



}