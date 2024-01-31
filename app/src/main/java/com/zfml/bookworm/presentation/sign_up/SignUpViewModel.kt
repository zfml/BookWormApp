package com.zfml.bookworm.presentation.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.bookworm.domain.model.Response
import com.zfml.bookworm.domain.repository.AuthRepository
import com.zfml.bookworm.domain.repository.SignUpResponse
import com.zfml.bookworm.presentation.sign_in.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {


    private val _signUpState = MutableStateFlow(SignInUiState())
    val signUpUiState = _signUpState.asStateFlow()

    fun signUpWithEmailAndPassword(email: String,password: String) {
        viewModelScope.launch {
            when(val signUpResponse = authRepository.signUpWithEmailAndPassword(email, password)) {
                is Response.Failure -> {
                    _signUpState.update {
                        it.copy(
                            error = signUpResponse.e.toString(),
                            isLoading = false
                        )
                    }
                }

                Response.Loading -> {
                    _signUpState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                is Response.Success -> {
                    _signUpState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                }
            }
        }
    }


}

data class SignUpUiState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

