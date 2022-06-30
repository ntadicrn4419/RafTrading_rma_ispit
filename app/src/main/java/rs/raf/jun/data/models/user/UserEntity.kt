package rs.raf.jun.data.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    val username: String,
    val email: String,
    val password: String,
    var accountBalance: Float,
    var portfolioValue: Float
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}