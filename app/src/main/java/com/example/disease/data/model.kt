import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data") val data: List<Post>,
    @SerializedName("links") val links: Links,
    @SerializedName("meta") val meta: Meta
)

data class Post(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: Category,
    @SerializedName("views_count") val viewsCount: Int,
    @SerializedName("type") val type: String,
    @SerializedName("cover_image") val coverImage: CoverImage?,
    @SerializedName("tags") val tags: List<Tag>,
    @SerializedName("created_at") val createdAt: String
)

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("icon") val icon: String?,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("level") val level: Int
)

data class CoverImage(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("size") val size: String,
    @SerializedName("disk") val disk: String
)

data class Tag(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

data class Links(
    @SerializedName("first") val first: String,
    @SerializedName("last") val last: String,
    @SerializedName("prev") val prev: String?,
    @SerializedName("next") val next: String?
)

data class Meta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("from") val from: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("links") val links: List<Link>,
    @SerializedName("path") val path: String,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("to") val to: Int,
    @SerializedName("total") val total: Int
)

data class Link(
    @SerializedName("url") val url: String?,
    @SerializedName("label") val label: String,
    @SerializedName("active") val active: Boolean
)