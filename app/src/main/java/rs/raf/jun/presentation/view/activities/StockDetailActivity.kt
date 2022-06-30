package rs.raf.jun.presentation.view.activities
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.jun.R
import rs.raf.jun.application.RafTradingApp.Companion.context
import rs.raf.jun.data.models.stocks.BarWithDate
import rs.raf.jun.data.models.stocks.SimpleStock
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.stocks.StockDetail
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.states.LocalPurchasedStockState
import rs.raf.jun.presentation.view.states.RemoteStockState
import rs.raf.jun.presentation.viewmodel.MainViewModel
import java.text.SimpleDateFormat


class StockDetailActivity  : AppCompatActivity(R.layout.activity_stock_detail) {
    private val mainViewModel: MainContract.ViewModel by viewModel<MainViewModel>()
    private var stockSymbol: String? = null
    private var stockName: String? = null
    private var stockModel: StockDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_detail)
        init()
    }

    private fun init(){
        val bundle :Bundle ?=intent.extras
        stockSymbol = bundle!!.getString("stockSymbol")
        stockName = bundle!!.getString("stockName")
        initObserver()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun generateUI() {
        findViewById<TextView>(R.id.stock_detail_symbol_tv).text = stockSymbol
        findViewById<TextView>(R.id.stock_detail_last_price_tv).text = stockModel!!.last.toString() + " " + stockModel!!.currency

        findViewById<TextView>(R.id.stock_detail_open).text = "open: " + stockModel!!.open.toString()
        findViewById<TextView>(R.id.stock_detail_bid).text = "bid: " + stockModel!!.bid.toString()
        findViewById<TextView>(R.id.stock_detail_close).text = "close: " + stockModel!!.close.toString()
        findViewById<TextView>(R.id.stock_detail_ask).text = "ask: " + stockModel!!.ask.toString()
        findViewById<TextView>(R.id.stock_detail_div_yeald).text = "div yeald: " + stockModel!!.metrics.alpha.toString()
        findViewById<TextView>(R.id.stock_detail_p_e).text = "p/e: " + stockModel!!.metrics.sharp.toString()
        findViewById<TextView>(R.id.stock_detail_mkt).text = "mkt: " + stockModel!!.metrics.marketCup.toString()
        findViewById<TextView>(R.id.stock_detail_cap).text = "cap: " + stockModel!!.metrics.sharp.toString()
        findViewById<TextView>(R.id.stock_detail_eps).text = "eps: " + stockModel!!.metrics.eps.toString()
        findViewById<TextView>(R.id.stock_detail_ebit).text = "ebit: " + stockModel!!.metrics.ebit.toString()
        findViewById<TextView>(R.id.stock_detail_beta).text = "beta: " + stockModel!!.metrics.beta.toString()

        generateChart()
    }

    private fun initObserver() {
        mainViewModel.remoteStockState.observe(this){
            renderRemoteState(it)
        }
        mainViewModel.getStockBySymbol(stockSymbol!!)

        mainViewModel.localPurchasedStockState.observe(this){
            renderLocalState(it)
        }
        val pref = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        val loggedUserEmail = pref.getString(LoginActivity.SharedPrefKeys.EMAIL_KEY, null)
        mainViewModel.getAllStocksByUserEmail(loggedUserEmail.toString())
    }


    private fun initListeners() {

        findViewById<Button>(R.id.buy_btn).setOnClickListener {
            val intent = Intent(this, BuyActivity::class.java)
            intent.putExtra("stockSymbol", stockSymbol)
            intent.putExtra("stockName", stockName)
            startActivity(intent)
        }

        findViewById<Button>(R.id.sell_btn).setOnClickListener {
            val intent = Intent(this, SellActivity::class.java)
            intent.putExtra("stockSymbol", "T")
            intent.putExtra("stockName", "AT&T, Inc.")
            startActivity(intent)
        }

    }

    private fun renderRemoteState(state: RemoteStockState) {
        when(state){
            is RemoteStockState.SuccessDetailRemoteState ->{
                stockModel = state.stockDetail
                generateUI()
            }
            is RemoteStockState.ErrorRemoteState ->{
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderLocalState(state: LocalPurchasedStockState) {
        when(state){
            is LocalPurchasedStockState.SuccessLocalState->{
                if(checkList(state.stocks)){
                    findViewById<Button>(R.id.sell_btn).visibility = View.VISIBLE
                }else{
                    findViewById<Button>(R.id.sell_btn).visibility = View.INVISIBLE
                }
            }
            is LocalPurchasedStockState.ErrorLocalState ->{
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkList(purchasedStocks: List<SimpleStock>): Boolean{
        for(stock in purchasedStocks){
            if(stock.symbol == stockModel!!.symbol){
                return true;
            }
        }
        return false;
    }



    @SuppressLint("SimpleDateFormat")
    private fun generateChart() {

        val barsWithDate = ArrayList<BarWithDate>()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        for(bar in stockModel?.chart!!.bars){
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
        val barData = BarData()
        barData.addDataSet(barDataSet)

        val stockDetailChart = findViewById<BarChart>(R.id.stock_chart)
        stockDetailChart.data = barData
        stockDetailChart.invalidate()
    }



}
