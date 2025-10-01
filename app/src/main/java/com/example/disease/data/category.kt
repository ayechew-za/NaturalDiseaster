package com.example.disease.data

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("status")
    val status: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: List<Category>? = null
)

data class Category(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("slug")
    val slug: String? = null,

    @SerializedName("icon")
    val icon: String? = null,

    @SerializedName("parent_id")
    val parentId: String? = null,

    @SerializedName("level")
    val level: Int? = null,

    @SerializedName("children")
    val children: List<Category>? = null
)