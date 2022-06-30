package rs.raf.jun.data.models.stocks

data class StockDetail(
    val instrumentId: String,
    val symbol: String,
    val name: String,
    val currency: String,
    val last: Float,
    val open: Float,
    val close: Float,
    val bid: Float,
    val ask: Float,
    val metrics: Metrics,
    val chart: Chart
)

