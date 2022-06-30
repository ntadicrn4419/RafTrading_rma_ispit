package rs.raf.jun.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.jun.data.models.stocks.Stock

class StockDiffCallback : DiffUtil.ItemCallback<Stock>()  {

    override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
        return oldItem.symbol == newItem.symbol && oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
        return oldItem.symbol == newItem.symbol && oldItem.name == newItem.name
    }
}