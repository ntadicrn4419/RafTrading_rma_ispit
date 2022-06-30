package rs.raf.jun.data.datasources.remote.mock.stock

import io.reactivex.Observable
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.stocks.StockDetailResponse
import rs.raf.jun.data.models.stocks.StockResponse

interface MockStockService {
    fun getAll(): Observable<List<StockResponse>>
    fun getBySymbol(symbol: String): Observable<StockDetailResponse>
}