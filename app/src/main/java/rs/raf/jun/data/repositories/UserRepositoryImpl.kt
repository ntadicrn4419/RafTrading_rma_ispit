package rs.raf.jun.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.datasources.local.UserDao
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.user.User
import rs.raf.jun.data.models.user.UserEntity

class UserRepositoryImpl (
    private val localDataSource: UserDao
): UserRepository{
    override fun insert(user: User): Completable {
        val userEntity =
            UserEntity(
                user.username,
                user.email,
                user.password,
                user.accountBalance,
                user.portfolioValue
             )
        return localDataSource.insert(userEntity)
    }

    override fun getAll(): Observable<List<User>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    User(
                        it.username,
                        it.email,
                        it.password,
                        it.accountBalance,
                        it.portfolioValue
                    )
                }
            }
    }

    override fun update(email: String, accountBalance: Float, portfolioValue: Float): Completable {
        return localDataSource.update(email, accountBalance, portfolioValue)
    }


}