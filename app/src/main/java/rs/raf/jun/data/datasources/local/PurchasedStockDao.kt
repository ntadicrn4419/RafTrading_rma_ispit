package rs.raf.jun.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.models.stocks.PurchasedStockEntity
import rs.raf.jun.data.models.user.PortfolioHistoryEntity
import rs.raf.jun.data.models.user.UserEntity

@Dao
abstract class PurchasedStockDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: PurchasedStockEntity): Completable

    @Query("SELECT * FROM stocks WHERE userEmail = :userEmail")
    abstract fun getAllByUserEmail(userEmail: String): Observable<List<PurchasedStockEntity>>

    @Query("DELETE FROM stocks WHERE id in (SELECT id FROM stocks WHERE instrumentId = :instrumentId LIMIT :numberOfSharesToDelete)")
    abstract fun delete(instrumentId: String, numberOfSharesToDelete: Int): Completable

    @Query("DELETE FROM stocks WHERE instrumentId = :instrumentId ")
    abstract fun deleteAll(instrumentId: String): Completable


}