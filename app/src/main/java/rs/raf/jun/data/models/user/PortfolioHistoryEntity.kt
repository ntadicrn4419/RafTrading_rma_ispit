package rs.raf.jun.data.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio_history")
data class PortfolioHistoryEntity (
    val time: String,
    val value: Float,
    val userEmail: String
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}