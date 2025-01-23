package com.example.todo_app.model

data class GiphyResponse(
    val data: Data,
) {
    data class Data(
        val type: String,
        val id: String,
        val url: String,
        val images: Images,
    ) {
        data class Images(
            val original: Original,
        ) {
            data class Original(
                val height: Int,
                val width: Int,
                val url: String,
            )
        }
    }
}
