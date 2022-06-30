package rs.raf.jun.data.models.stocks

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recommendation(
    val buyRecommendationsPct: Float,
    val sellRecommendationsPct: Float,
    val holdRecommendationsPct: Float
)