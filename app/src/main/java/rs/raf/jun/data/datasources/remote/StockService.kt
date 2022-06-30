package rs.raf.jun.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.jun.data.models.stocks.StockResponse

interface StockService {
    @GET("stocks")
    fun getAll(@Query("limit") limit: Int = 1000): Observable<List<StockResponse>>

}