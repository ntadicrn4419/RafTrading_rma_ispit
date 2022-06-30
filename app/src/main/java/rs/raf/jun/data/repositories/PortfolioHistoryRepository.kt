package rs.raf.jun.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.user.PortfolioHistory

interface PortfolioHistoryRepository {
    fun insert(userEmail: String, portfolioHistory: PortfolioHistory): Completable
    fun getAllByUserEmail(userEmail: String): Observable<List<PortfolioHistory>>
}