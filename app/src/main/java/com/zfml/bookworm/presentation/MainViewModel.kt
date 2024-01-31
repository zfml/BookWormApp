package com.zfml.bookworm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.bookworm.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
   private val authRepository: AuthRepository
): ViewModel() {
    init {
       getAuthState()
    }

    fun getAuthState() = authRepository.getAuthState(viewModelScope)
}