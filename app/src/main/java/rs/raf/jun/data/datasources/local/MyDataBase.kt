package rs.raf.jun.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.raf.jun.data.models.stocks.PurchasedStockEntity
import rs.raf.jun.data.models.user.PortfolioHistoryEntity
import rs.raf.jun.data.models.user.UserEntity

@Database(
    entities = [UserEntity::class, PortfolioHistoryEntity::class, PurchasedStockEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters()
abstract class MyDataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getPortfolioHistoryDao(): PortfolioHistoryDao
    abstract fun getPurchasedStockDao(): PurchasedStockDao
}