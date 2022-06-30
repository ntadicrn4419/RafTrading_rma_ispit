package rs.raf.jun.data.datasources.remote.mock.stock
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import rs.raf.jun.application.RafTradingApp
import rs.raf.jun.data.models.stocks.StockDetailResponse
import rs.raf.jun.data.models.stocks.StockResponse
import java.io.BufferedReader
import java.io.InputStreamReader

class MockStockServiceImpl :MockStockService{
    override fun getAll(): Observable<List<StockResponse>> {

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<List<StockResponse>>(Types.newParameterizedType(List::class.java, StockResponse::class.java))
        val file = RafTradingApp.context?.assets?.open("getIndexes.json")
        val isr = InputStreamReader(file)
        val reader = BufferedReader(isr)

        val jsonString = reader.readText()

        val listStockResponse = jsonAdapter.fromJson(jsonString)
        return Observable.fromArray(listStockResponse)
    }


    override fun getBySymbol(symbol: String): Observable<StockDetailResponse> {

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(StockDetailResponse::class.java)
        val file = RafTradingApp.context?.assets?.open("searchQuote_symbol=T.json")
        val isr = InputStreamReader(file)
        val reader = BufferedReader(isr)

        val jsonString = reader.readText()

        val stockDetailResponse = jsonAdapter.fromJson(jsonString)
        return Observable.just(stockDetailResponse)
    }

}
