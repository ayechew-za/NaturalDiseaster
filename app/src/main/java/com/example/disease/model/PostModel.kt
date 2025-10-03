package com.example.disease.model

data class PostModel(
    val categoryId: String? = null,
    val type: String? = null, // "new" or "knowledge"
    val query: String? = null
)