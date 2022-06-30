package rs.raf.jun.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.datasources.local.PortfolioHistoryDao
import rs.raf.jun.data.models.user.PortfolioHistory
import rs.raf.jun.data.models.user.PortfolioHistoryEntity

class PortfolioHistoryRepositoryImpl (
    private val localDataSource: PortfolioHistoryDao
): PortfolioHistoryRepository{
    override fun insert(userEmail: String, portfolioHistory: PortfolioHistory): Completable {
        val entity = PortfolioHistoryEntity(
            portfolioHistory.time,
            portfolioHistory.value,
            userEmail
        )
        return localDataSource.insert(entity)
    }

    override fun getAllByUserEmail(userEmail: String): Observable<List<PortfolioHistory>> {
        return localDataSource
            .getAllByUserEmail(userEmail)
            .map {
                it.map {
                    PortfolioHistory(
                        it.time,
                        it.value
                    )
                }
            }
    }

}