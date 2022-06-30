package rs.raf.jun.data.models.stocks
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class PurchasedStockEntity (
    val instrumentId: String,
    val symbol: String,
    val name: String,
    val currency: String,
    val lastPrice: Float,
    val changeFromPreviousClose: Float,
    val percentChangeFromPreviousClose: Float,
    val marketName: String,

    val userEmail: String
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}