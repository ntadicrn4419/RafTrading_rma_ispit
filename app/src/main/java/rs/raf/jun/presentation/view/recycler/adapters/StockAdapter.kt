package rs.raf.jun.presentation.view.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.databinding.LayoutItemStockBinding
import rs.raf.jun.presentation.view.recycler.diff.StockDiffCallback
import rs.raf.jun.presentation.view.recycler.viewholder.StockViewHolder

class StockAdapter (
    private val funClickListener: (stock: Stock) -> Unit
): ListAdapter<Stock, StockViewHolder>(StockDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemBinding = LayoutItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(itemBinding, {this.funClickListener(getItem(it))})
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}