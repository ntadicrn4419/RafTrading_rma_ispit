package rs.raf.jun.data.models.stocks

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Metrics(
    val alpha: Float,
    val beta: Float,
    val sharp: Float,
    val marketCup: Float,
    val eps: Float,
    val ebit: Float
)
