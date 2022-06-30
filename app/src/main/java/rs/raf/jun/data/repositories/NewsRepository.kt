package rs.raf.jun.data.repositories

import io.reactivex.Observable
import rs.raf.jun.data.models.Resource
import rs.raf.jun.data.models.news.News

interface NewsRepository {
    fun fetchAll(): Observable<List<News>>
}