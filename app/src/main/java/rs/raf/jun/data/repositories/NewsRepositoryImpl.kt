package rs.raf.jun.data.repositories

import io.reactivex.Observable
import rs.raf.jun.data.datasources.remote.mock.news.MockNewsService
import rs.raf.jun.data.models.news.News

class NewsRepositoryImpl(
    //private val remoteDataSource: NewsService
    private val remoteDataSource: MockNewsService

): NewsRepository{
    override fun fetchAll(): Observable<List<News>> {
        return remoteDataSource
            .getAll()
            .map {
                it.map {
                    News(
                        it.title,
                        it.content,
                        it.link,
                        it.date,
                        it.image
                    )
                }
            }
    }

}