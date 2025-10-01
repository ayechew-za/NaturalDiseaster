package com.example.disease.data

import com.google.gson.annotations.SerializedName

data class AnnouncementResponse(
    @SerializedName("status")
    val status: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: Announcement? = null
)

data class Announcement(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("style")
    val style: Style? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)

data class Style(
    @SerializedName("bgColor")
    val bgColor: String? = null,

    @SerializedName("textColor")
    val textColor: String? = null,

    @SerializedName("borderColor")
    val borderColor: String? = null
)
