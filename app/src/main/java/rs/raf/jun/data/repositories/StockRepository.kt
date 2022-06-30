package rs.raf.jun.data.repositories
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.models.stocks.PurchasedStockEntity
import rs.raf.jun.data.models.stocks.SimpleStock
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.stocks.StockDetail
import rs.raf.jun.data.models.user.UserEntity

interface StockRepository {
    fun fetchAll(): Observable<List<Stock>>
    fun insert(stock: SimpleStock): Completable
    fun fetchStockBySymbol(symbol: String): Observable<StockDetail>
    fun getAllByUserEmail(userEmail: String): Observable<List<SimpleStock>>
    fun delete(simpleStock: SimpleStock, numberOfSharesToDelete: Int): Completable
    fun deleteAll(simpleStock: SimpleStock): Completable
}