package rs.raf.jun.data.datasources.remote.mock.news

import io.reactivex.Observable
import rs.raf.jun.data.models.news.NewsResponse

interface MockNewsService {
    fun getAll(): Observable<List<NewsResponse>>
}