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
import rs.raf.jun.presentation.view.states.RemoteStockState
import rs.raf.jun.presentation.viewmodel.MainViewModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level.parse

class BuyActivity : AppCompatActivity() {

    private val mainViewModel: MainContract.ViewModel by viewModel<MainViewModel>()
    private var stockSymbol: String? = null
    private var stockName: String? = null
    private var stockModel: StockDetail? = null
    private var simpleStock: SimpleStock? = null

    private var pref: SharedPreferences? = null
    private var userEmail: String? = null
    private var accountBalance: Float? = null
    private var portfolioValue: Float? = null
    private var buyWithShares: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
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
        findViewById<Button>(R.id.buy_shares_btn).setOnClickListener {

            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val currentTime = sdf.format(Date())

            if(!this.buyWithShares){
                val amountStr  = findViewById<EditText>(R.id.amount_of_money_et).text
                if(!validateAmount(amountStr.toString())){
                    return@setOnClickListener
                }
                val amount = amountStr.toString().toFloat()
                val number = (amount / stockModel!!.last).toInt()
                for(i in 0 until number){
                    mainViewModel.addStock(simpleStock!!)
                }
                updateInMemoryPortfolio(accountBalance!! - amount, portfolioValue!! + amount)
                mainViewModel.updateUserPortfolio(userEmail.toString(), accountBalance!!, portfolioValue!!)
                findViewById<TextView>(R.id.account_state_tv).text = accountBalance!!.toString()

                mainViewModel.insertPortfolioHistoryNode(userEmail!!, PortfolioHistory(currentTime.toString(), portfolioValue!!))

            }else{

                val shareNumStr = findViewById<EditText>(R.id.share_number_et).text
                if(!validateShareNumber(shareNumStr.toString())){
                    return@setOnClickListener
                }
                val shareNum = Integer.parseInt(shareNumStr.toString())

                for(i in 0 until shareNum){
                    mainViewModel.addStock(simpleStock!!)
                }

                mainViewModel.addStock(simpleStock!!)
                updateInMemoryPortfolio(accountBalance!! - simpleStock!!.lastPrice*shareNum, portfolioValue!! + simpleStock!!.lastPrice*shareNum)
                mainViewModel.updateUserPortfolio(userEmail.toString(), accountBalance!!, portfolioValue!!)
                findViewById<TextView>(R.id.account_state_tv).text = accountBalance!!.toString()

                mainViewModel.insertPortfolioHistoryNode(userEmail!!, PortfolioHistory(currentTime.toString(), portfolioValue!!))
            }

        }
        findViewById<Switch>(R.id.buy_stock_switch).setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                findViewById<EditText>(R.id.amount_of_money_et).visibility = View.INVISIBLE
                findViewById<EditText>(R.id.share_number_et).visibility = View.VISIBLE
                this.buyWithShares = true
            }else{
                findViewById<EditText>(R.id.amount_of_money_et).visibility = View.VISIBLE
                findViewById<EditText>(R.id.share_number_et).visibility = View.INVISIBLE
                this.buyWithShares = false
            }
        }
    }

    private fun validateAmount(amountStr: String): Boolean{
        if(amountStr == ""){
            Toast.makeText(applicationContext, "You have to enter amount you want to spend!", Toast.LENGTH_SHORT).show()
            return false
        }
        var amount: Float? = null
        try {
            amount = amountStr.toString().toFloat()

        }catch (e: Exception){
            Toast.makeText(applicationContext, "Wrong input. Amount must be number", Toast.LENGTH_SHORT).show()
            return false
        }
        if(amount!! > accountBalance!!){
            Toast.makeText(applicationContext, "You don't have enough money on your account balance!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(amount!! < stockModel!!.last){
            Toast.makeText(applicationContext, "Amount is not enough to buy this share", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateShareNumber(shareNumStr: String):Boolean{
        if(shareNumStr == ""){
            Toast.makeText(applicationContext, "You have to enter amount you want to spend!", Toast.LENGTH_SHORT).show()
            return false
        }
        var shareNum: Int? = null
        try {
            shareNum = Integer.parseInt(shareNumStr)

        }catch (e: Exception){
            Toast.makeText(applicationContext, "Wrong input. Share number must be whole number", Toast.LENGTH_SHORT).show()
            return false
        }
        if(shareNum!! * stockModel!!.last > accountBalance!!){
            Toast.makeText(applicationContext, "You don't have enough money on your account balance!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
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
        findViewById<TextView>(R.id.account_state_tv).text = accountBalance.toString()
    }
}