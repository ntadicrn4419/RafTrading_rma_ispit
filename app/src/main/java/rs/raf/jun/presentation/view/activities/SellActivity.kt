package rs.raf.jun.presentation.view.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.jun.R
import rs.raf.jun.data.models.stocks.SimpleStock
import rs.raf.jun.data.models.stocks.StockDetail
import rs.raf.jun.data.models.user.PortfolioHistory
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.states.LocalPurchasedStockState
import rs.raf.jun.presentation.view.states.RemoteStockState
import rs.raf.jun.presentation.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class SellActivity : AppCompatActivity() {

    private val mainViewModel: MainContract.ViewModel by viewModel<MainViewModel>()
    private var stockSymbol: String? = null
    private var stockName: String? = null
    private var stockModel: StockDetail? = null
    private var simpleStock: SimpleStock? = null

    private var pref: SharedPreferences? = null
    private var userEmail: String? = null
    private var accountBalance: Float? = null
    private var portfolioValue: Float? = null
    private var shareNumber: Int? = null
    private var deleteAll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        init()
    }
    private fun init(){
        val bundle :Bundle ?=intent.extras
        stockSymbol = bundle!!.getString("stockSymbol")
        stockName = bundle.getString("stockName")

        this.pref = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        userEmail = pref?.getString(LoginActivity.SharedPrefKeys.EMAIL_KEY, null)
        accountBalance = pref?.getFloat(LoginActivity.SharedPrefKeys.ACCOUNT_BALANCE_KEY, 0f)
        portfolioValue = pref?.getFloat(LoginActivity.SharedPrefKeys.PORTFOLIO_VALUE_KEY, 0f)

        initObserver()
        initListeners()
    }

    private fun initListeners() {

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentTime = sdf.format(Date())

        findViewById<Button>(R.id.sell_shares_btn).setOnClickListener {
            //Sluzi da kad se okine observer ukoliko nemamo vise ovih akcija, da sklonimo dugme Sell i update-ujemo broj deonica.
            mainViewModel.getAllStocksByUserEmail(userEmail!!)

            if(deleteAll){
                mainViewModel.deleteAllStocks(simpleStock!!)
                updateInMemoryPortfolio(accountBalance!! + simpleStock!!.lastPrice*shareNumber!!, portfolioValue!! - simpleStock!!.lastPrice*shareNumber!!)
                mainViewModel.updateUserPortfolio(userEmail.toString(), accountBalance!!, portfolioValue!!)
                mainViewModel.insertPortfolioHistoryNode(userEmail!!, PortfolioHistory(currentTime.toString(), portfolioValue!!))
            }else{
                val numberToSell = findViewById<EditText>(R.id.shares_number_to_sell_et).text
                var toSell = 1
                if(numberToSell.toString() != ""){
                    toSell = Integer.parseInt(numberToSell.toString())
                }
                if(toSell > shareNumber!!) {
                    Toast.makeText(applicationContext, "You don't have that much shares!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mainViewModel.deleteStock(simpleStock!!, toSell)
                updateInMemoryPortfolio(accountBalance!! + simpleStock!!.lastPrice * toSell, portfolioValue!! - simpleStock!!.lastPrice * toSell)
                mainViewModel.updateUserPortfolio(userEmail.toString(), accountBalance!!, portfolioValue!!)
                mainViewModel.insertPortfolioHistoryNode(userEmail!!, PortfolioHistory(currentTime.toString(), portfolioValue!!))
            }
        }
        findViewById<Switch>(R.id.sell_stock_switch).setOnCheckedChangeListener{ _, isChecked ->
            deleteAll = isChecked
        }
    }

    private fun updateInMemoryPortfolio(accountBalance: Float, portfolioValue: Float){
        this.accountBalance = accountBalance
        this.portfolioValue = portfolioValue
        writeToSharedPref(accountBalance, portfolioValue)
    }

    private fun writeToSharedPref(accountBalance: Float, portfolioValue: Float) {
        val editor = this.pref?.edit()
        editor?.putFloat(LoginActivity.SharedPrefKeys.ACCOUNT_BALANCE_KEY, accountBalance)
        editor?.putFloat(LoginActivity.SharedPrefKeys.PORTFOLIO_VALUE_KEY, portfolioValue)
        editor?.apply()
    }

    private fun initObserver() {
        mainViewModel.remoteStockState.observe(this){
            renderRemoteState(it)
        }
        mainViewModel.getStockBySymbol(stockSymbol!!)

        mainViewModel.localPurchasedStockState.observe(this){
            renderLocalState(it)
        }
        mainViewModel.getAllStocksByUserEmail(userEmail!!)
    }
    private fun renderLocalState(state: LocalPurchasedStockState) {
        when(state){
            is LocalPurchasedStockState.SuccessLocalState->{
                shareNumber = countShares(state.stocks)
                findViewById<TextView>(R.id.shares_number_tv).text = shareNumber.toString()
                if(shareNumber == 0){
                    findViewById<Button>(R.id.sell_shares_btn).visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "You've sold all your shares!", Toast.LENGTH_SHORT).show()
                }
            }
            is LocalPurchasedStockState.ErrorLocalState ->{
                Toast.makeText(applicationContext, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun countShares(purchasedStocks: List<SimpleStock>): Int{
        var cnt = 0
        for(stock in purchasedStocks){
            if(stock.symbol == stockModel!!.symbol){
                cnt++
            }
        }
        return cnt
    }

    private fun renderRemoteState(state: RemoteStockState) {
        when(state){
            is RemoteStockState.SuccessDetailRemoteState ->{
                stockModel = state.stockDetail
                simpleStock = convertStockDetailToSimpleStock(stockModel!!)
                generateUI()
            }
            is RemoteStockState.ErrorRemoteState ->{
                Toast.makeText(applicationContext, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertStockDetailToSimpleStock(stockDetail: StockDetail): SimpleStock{
        return  SimpleStock(
            stockDetail.instrumentId,
            stockDetail.symbol,
            stockDetail.name,
            stockDetail.currency,
            stockDetail.last,
            stockDetail.close,
            stockDetail.close,
            stockDetail.symbol + "-" + stockDetail.name,
            userEmail.toString()
        )
    }


    @SuppressLint("SetTextI18n")
    private fun generateUI() {
        findViewById<TextView>(R.id.stock_name_tv).text = "$stockSymbol - $stockName"
    }
}