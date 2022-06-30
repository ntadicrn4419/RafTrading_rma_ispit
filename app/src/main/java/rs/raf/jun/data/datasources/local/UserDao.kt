package rs.raf.jun.data.datasources.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.models.user.UserEntity

@Dao
abstract class UserDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: UserEntity): Completable

    @Query("SELECT * FROM users")
    abstract fun getAll(): Observable<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    abstract fun getById(id: Int): Observable<List<UserEntity>>

    @Query("DELETE FROM users WHERE id = :id")
    abstract fun deleteById(id: Int): Completable

    @Query("UPDATE users SET accountBalance = :accountBalance, portfolioValue = :portfolioValue WHERE email = :email")
    abstract fun update(email: String, accountBalance: Float, portfolioValue: Float): Completable



}