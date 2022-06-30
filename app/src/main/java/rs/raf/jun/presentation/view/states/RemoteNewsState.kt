package rs.raf.jun.presentation.view.states
import rs.raf.jun.data.models.news.News

sealed class RemoteNewsState {
    data class SuccessRemoteState(val news: List<News>): RemoteNewsState()
    data class ErrorRemoteState(val message: String): RemoteNewsState()
}