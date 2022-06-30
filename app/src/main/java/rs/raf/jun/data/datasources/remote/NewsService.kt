package rs.raf.jun.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.jun.data.models.news.NewsResponse

interface NewsService {
    @GET("news")
    fun getAll(@Query("limit") limit: Int = 1000): Observable<List<NewsResponse>>
}