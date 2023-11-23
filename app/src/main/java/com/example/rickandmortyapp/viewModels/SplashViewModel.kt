package com.example.rickandmortyapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val _navigateToNextScreen = MutableStateFlow(false)
    val navigateToNextScreen = _navigateToNextScreen.asStateFlow()


    init {
        // Scope for giving 2 seconds duration
        viewModelScope.launch {
            delay(2000)
            _navigateToNextScreen.value = true
        }
    }
}