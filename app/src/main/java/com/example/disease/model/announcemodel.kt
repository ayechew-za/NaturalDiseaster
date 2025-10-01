package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disease.data.AnnouncementResponse
import com.example.disease.data.network.RetrofitClient
import com.example.disease.data.repo.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AnnouncementUiState {
    object Loading : AnnouncementUiState()
    data class Success(val response: AnnouncementResponse) : AnnouncementUiState()
    data class Error(val message: String) : AnnouncementUiState()
}

class AnnouncementViewModel : ViewModel() {

    private val repository = AnnouncementRepository(RetrofitClient.apiService)

    private val _uiState = MutableStateFlow<AnnouncementUiState>(AnnouncementUiState.Loading)
    val uiState: StateFlow<AnnouncementUiState> = _uiState.asStateFlow()

    init {
        fetchAnnouncements()
    }

    fun fetchAnnouncements() {
        viewModelScope.launch {
            _uiState.value = AnnouncementUiState.Loading
            val result = repository.getAnnouncements()
            if (result.isSuccess) {
                _uiState.value = AnnouncementUiState.Success(result.getOrThrow())
            } else {
                _uiState.value = AnnouncementUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun retry() {
        fetchAnnouncements()
    }
}
