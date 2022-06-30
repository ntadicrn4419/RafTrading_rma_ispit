package rs.raf.jun.data.models.stocks

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockResponse (
    val instrumentId: String,
    val symbol: String,
    val name: String,
    val currency: String,
    val last: Float,
    val changeFromPreviousClose: Float,
    val percentChangeFromPreviousClose: Float,
    val marketName: String,
    val recommendation: Recommendation,
    val chart: Chart
)





