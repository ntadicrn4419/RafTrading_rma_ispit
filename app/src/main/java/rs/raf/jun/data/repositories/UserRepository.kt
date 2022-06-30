package rs.raf.jun.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.jun.data.models.user.User
import rs.raf.jun.data.models.user.UserEntity

interface UserRepository {
    fun insert(user: User): Completable
    fun getAll(): Observable<List<User>>
    fun update(email: String, accountBalance:Float, portfolioValue: Float): Completable
}