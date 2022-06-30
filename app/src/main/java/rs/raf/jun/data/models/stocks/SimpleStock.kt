package rs.raf.jun.data.models.stocks

data class SimpleStock(
    val instrumentId: String,
    val symbol: String,
    val name: String,
    val currency: String,
    val lastPrice: Float,
    val changeFromPreviousClose: Float,
    val percentChangeFromPreviousClose: Float,
    val marketName: String,
    val userEmail: String// user(owner)email- email onog ko je kupio ovaj stock
)