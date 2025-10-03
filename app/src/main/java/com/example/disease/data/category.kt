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

// Post data model for the detail page
// Post.kt


data class PostResponse(
    @SerializedName("data")
    val data: List<Post>? = null,

    @SerializedName("links")
    val links: Links? = null,

    @SerializedName("meta")
    val meta: Meta? = null
)

data class Post(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("category")
    val category: Category? = null,

    @SerializedName("views_count")
    val viewsCount: Int? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("cover_image")
    val coverImage: CoverImage? = null,

    @SerializedName("description_images") // ✅ ADD THIS FIELD
    val descriptionImages: List<DescriptionImage>? = null,

    @SerializedName("tags")
    val tags: List<Tag>? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)

data class CoverImage(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("size")
    val size: String? = null,

    @SerializedName("disk")
    val disk: String? = null,

    @SerializedName("alt")
    val alt: String? = null
)

// ✅ ADD DescriptionImage data class
data class DescriptionImage(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("size")
    val size: String? = null,

    @SerializedName("disk")
    val disk: String? = null
)

// ... other data classes remain the same
data class Tag(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("slug")
    val slug: String? = null
)

data class Links(
    @SerializedName("first")
    val first: String? = null,

    @SerializedName("last")
    val last: String? = null,

    @SerializedName("prev")
    val prev: String? = null,

    @SerializedName("next")
    val next: String? = null
)

data class Meta(
    @SerializedName("current_page")
    val currentPage: Int? = null,

    @SerializedName("from")
    val from: Int? = null,

    @SerializedName("last_page")
    val lastPage: Int? = null,

    @SerializedName("links")
    val links: List<Link>? = null,

    @SerializedName("path")
    val path: String? = null,

    @SerializedName("per_page")
    val perPage: Int? = null,

    @SerializedName("to")
    val to: Int? = null,

    @SerializedName("total")
    val total: Int? = null
)

data class Link(
    @SerializedName("url")
    val url: String? = null,

    @SerializedName("label")
    val label: String? = null,

    @SerializedName("active")
    val active: Boolean? = null
)