package rs.raf.jun.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.models.user.PortfolioHistoryEntity

@Dao
abstract class PortfolioHistoryDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: PortfolioHistoryEntity): Completable

    @Query("SELECT * FROM portfolio_history WHERE userEmail = :userEmail")
    abstract fun getAllByUserEmail(userEmail: String): Observable<List<PortfolioHistoryEntity>>
}