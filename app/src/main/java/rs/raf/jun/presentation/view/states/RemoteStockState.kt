package rs.raf.jun.presentation.view.states
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.stocks.StockDetail

sealed class RemoteStockState {
    data class SuccessRemoteState(val stocks: List<Stock>): RemoteStockState()
    data class SuccessDetailRemoteState(val stockDetail: StockDetail): RemoteStockState()
    data class ErrorRemoteState(val message: String): RemoteStockState()
}