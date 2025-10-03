package com.example.disease.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disease.data.repo.AnnouncementRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AnnouncementRepository) : ViewModel() {

    private val _selectedCategory = mutableStateOf<Category?>(null)
    val selectedCategory: State<Category?> = _selectedCategory

    private val _selectedSubcategory = mutableStateOf<Category?>(null)
    val selectedSubcategory: State<Category?> = _selectedSubcategory

    private val _subcategoryPosts = mutableStateOf<List<Post>>(emptyList())
    val subcategoryPosts: State<List<Post>> = _subcategoryPosts

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun selectSubcategory(subcategory: Category) {
        _selectedSubcategory.value = subcategory
        _selectedCategory.value = null
        loadSubcategoryPosts(subcategory.id ?: "")
    }

    private fun loadSubcategoryPosts(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getPostsByCategory(categoryId)

            _isLoading.value = false

            if (result.isSuccess) {
                _subcategoryPosts.value = result.getOrNull()?.data ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _subcategoryPosts.value = emptyList()
            }
        }
    }

    fun clearSubcategorySelection() {
        _selectedSubcategory.value = null
        _subcategoryPosts.value = emptyList()
        _error.value = null
    }
}
