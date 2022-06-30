package rs.raf.jun.presentation.view.recycler.viewholder
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import rs.raf.jun.data.models.stocks.BarWithDate
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.databinding.LayoutItemStockBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StockViewHolder (
    private val itemBinding: LayoutItemStockBinding,
    private val funClickListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    init{
        itemBinding.cardView.setOnClickListener{
            funClickListener(layoutPosition)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(stock: Stock){
        itemBinding.stockSymbolAndName.text = stock.symbol + " - " + stock.name
        itemBinding.stockLastPrice.text = stock.lastPrice.toString() + " " + stock.currency
        generateChart(stock)
    }

    private fun generateChart(stock: Stock) {

        val barsWithDate = ArrayList<BarWithDate>()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        for(bar in stock.chart.bars){
            val barWithDate = BarWithDate(bar.price, formatter.parse(bar.time))
            barsWithDate.add(barWithDate)
        }
        barsWithDate.sortedBy { bar -> bar.time }

        val entries = ArrayList<BarEntry>()
        var cnt = 0
        for (bar in barsWithDate){
            entries.add(BarEntry(cnt.toFloat(), bar.price))
            cnt++
        }

        val barDataSet = BarDataSet(entries, "Price")
        if(stock.changeFromPreviousClose < 0){
            barDataSet.color = Color.RED
        }else{
            barDataSet.color = Color.GREEN
        }
        val barData = BarData()
        barData.addDataSet(barDataSet)

        itemBinding.stockChart.data = barData
        itemBinding.stockChart.invalidate()
    }
}