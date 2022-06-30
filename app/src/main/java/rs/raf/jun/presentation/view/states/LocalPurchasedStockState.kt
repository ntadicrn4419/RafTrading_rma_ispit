package rs.raf.jun.presentation.view.states

import rs.raf.jun.data.models.stocks.SimpleStock
import rs.raf.jun.data.models.stocks.StockDetail

sealed class LocalPurchasedStockState {
    data class AddedStockLocalState(val message: String): LocalPurchasedStockState()
    data class DeletedStockLocalState(val message: String): LocalPurchasedStockState()
    data class SuccessLocalState(val stocks: List<SimpleStock>): LocalPurchasedStockState()
    data class SuccessDetailLocalState(val stockDetail: StockDetail): LocalPurchasedStockState()
    data class ErrorLocalState(val message: String): LocalPurchasedStockState()
}