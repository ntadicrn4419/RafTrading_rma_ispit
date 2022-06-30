package rs.raf.jun.presentation.view.states

import rs.raf.jun.data.models.user.PortfolioHistory

sealed class LocalPortfolioHistoryState {
    data class GotAllHistory(val portfolioHistoryList: List<PortfolioHistory>): LocalPortfolioHistoryState()
    data class AddedInHistory(val message: String): LocalPortfolioHistoryState()
    data class Error(val message: String): LocalPortfolioHistoryState()
}