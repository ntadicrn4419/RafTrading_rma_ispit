package rs.raf.jun.data.models.user

data class User(
    val username: String,
    val email: String,
    val password: String,
    var accountBalance: Float,
    var portfolioValue: Float
)