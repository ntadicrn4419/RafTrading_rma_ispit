package rs.raf.jun.presentation.contract

import androidx.lifecycle.LiveData
import rs.raf.jun.data.models.stocks.SimpleStock
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.user.PortfolioHistory
import rs.raf.jun.data.models.user.User
import rs.raf.jun.presentation.view.states.*

interface MainContract {
    interface ViewModel {
        val remoteNewsState: LiveData<RemoteNewsState>
        val localUserState: LiveData<LocalUserState>
        val remoteStockState: LiveData<RemoteStockState>
        val localPurchasedStockState: LiveData<LocalPurchasedStockState>
        val localPortfolioHistoryState: LiveData<LocalPortfolioHistoryState>

        fun addUser(user: User)
        fun getAllUsers()
        fun updateUserPortfolio(userEmail: String, accountBalance:Float, portfolioValue: Float)

        fun fetchStocks()
        fun addStock(simpleStock: SimpleStock)
        fun getStockBySymbol(symbol: String)
        fun getAllStocksByUserEmail(userEmail: String)
        fun deleteStock(simpleStock: SimpleStock, numberOfSharesToDelete: Int)
        fun deleteAllStocks(simpleStock: SimpleStock)

        fun insertPortfolioHistoryNode(userEmail: String, portfolioHistory: PortfolioHistory)
        fun getAllPortfolioHistoryNodesByUserEmail(userEmail: String)

        fun fetchNews()
    }
}