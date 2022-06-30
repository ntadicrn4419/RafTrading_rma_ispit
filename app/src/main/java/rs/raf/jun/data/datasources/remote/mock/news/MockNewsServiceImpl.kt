package rs.raf.jun.data.datasources.remote.mock.news

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import rs.raf.jun.application.RafTradingApp.Companion.context
import rs.raf.jun.data.models.news.NewsResponse
import java.io.BufferedReader
import java.io.InputStreamReader

class MockNewsServiceImpl : MockNewsService {
    override fun getAll(): Observable<List<NewsResponse>> {
        //citanje iz fajla getNews.json
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<List<NewsResponse>>(Types.newParameterizedType(List::class.java, NewsResponse::class.java))
        val file = context?.assets?.open("getNews.json")
        val isr = InputStreamReader(file)
        val reader = BufferedReader(isr)

        val jsonString = reader.readText()

        val listNewsResponse = jsonAdapter.fromJson(jsonString)
        return Observable.fromArray(listNewsResponse)
    }

}