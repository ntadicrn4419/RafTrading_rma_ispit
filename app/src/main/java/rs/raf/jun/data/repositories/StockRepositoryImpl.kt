package rs.raf.jun.data.repositories
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.datasources.local.PurchasedStockDao
import rs.raf.jun.data.datasources.remote.mock.stock.MockStockService
import rs.raf.jun.data.models.stocks.*



class StockRepositoryImpl(
    private val localDataSource: PurchasedStockDao,
    //private val remoteDataSource: StockService
    private val remoteDataSource: MockStockService

): StockRepository{
    override fun fetchAll(): Observable<List<Stock>> {
        return remoteDataSource
            .getAll()
            .map {
                it.map {
                    Stock(
                        it.instrumentId,
                        it.symbol,
                        it.name,
                        it.currency,
                        it.last,
                        it.changeFromPreviousClose,
                        it.percentChangeFromPreviousClose,
                        it.marketName,
                        it.recommendation,
                        it.chart
                    )
                }
            }
    }



    override fun insert(stock: SimpleStock): Completable {
        val stockEntity =
            PurchasedStockEntity(
                stock.instrumentId,
                stock.symbol,
                stock.name,
                stock.currency,
                stock.lastPrice,
                stock.changeFromPreviousClose,
                stock.percentChangeFromPreviousClose,
                stock.marketName,
                stock.userEmail
            )
        return localDataSource.insert(stockEntity)
    }

    override fun fetchStockBySymbol(symbol: String): Observable<StockDetail> {
        return remoteDataSource
            .getBySymbol(symbol)
            .map {
                    StockDetail(
                        it.instrumentId,
                        it.symbol,
                        it.name,
                        it.currency,
                        it.last,
                        it.open,
                        it.close,
                        it.bid,
                        it.ask,
                        it.metrics,
                        it.chart
                    )
            }

    }

    override fun getAllByUserEmail(email: String): Observable<List<SimpleStock>> {
        return localDataSource
            .getAllByUserEmail(email)
            .map {
                it.map { SimpleStock(
                        it.instrumentId,
                        it.symbol,
                        it.name,
                        it.currency,
                        it.lastPrice,
                        it.changeFromPreviousClose,
                        it.percentChangeFromPreviousClose,
                        it.marketName,
                        email
                    )
                }
            }
    }

    override fun delete(simpleStock: SimpleStock, numberOfSharesToDelete: Int): Completable {
        return localDataSource.delete(simpleStock.instrumentId, numberOfSharesToDelete)
    }

    override fun deleteAll(simpleStock: SimpleStock): Completable {
        return localDataSource.deleteAll(simpleStock.instrumentId)
    }

}