package rs.raf.jun.data.models.stocks

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chart(
    val bars: List<Bar>
)