package com.zfml.bookworm.presentation.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.AuthRepository
import com.zfml.bookworm.domain.repository.SignInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState = _signInUiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        _signInUiState.value = SignInUiState(isLoading = true)

        when(val response = authRepository.signInWithEmailAndPassword(email, password)) {
            is Response.Failure -> {
                _signInUiState.update {
                    it.copy(error = response.e?.localizedMessage)
                }
            }

            Response.Loading -> {
                _signInUiState.update {
                    it.copy(isLoading = true)
                }
            }
            is Response.Success -> {
                _signInUiState.update {
                    it.copy(isSuccess = true)
                }
            }
        }

    }

}

data class SignInUiState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = ""
)