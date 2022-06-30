package rs.raf.jun.data.models.stocks

data class Stock (
    val instrumentId: String,
    val symbol: String,
    val name: String,
    val currency: String,
    val lastPrice: Float,
    val changeFromPreviousClose: Float,
    val percentChangeFromPreviousClose: Float,
    val marketName: String,
    val recommendation: Recommendation,
    val chart: Chart
)
