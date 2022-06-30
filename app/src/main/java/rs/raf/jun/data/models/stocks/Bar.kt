package rs.raf.jun.data.models.stocks
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Bar(
    val price: Float,
    val time: String
)