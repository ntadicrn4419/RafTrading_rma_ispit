package rs.raf.jun.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.jun.R
import rs.raf.jun.data.models.stocks.BarWithDate
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.data.models.user.PortfolioHistory
import rs.raf.jun.databinding.FragmentPortfolioBinding
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.activities.LoginActivity
import rs.raf.jun.presentation.view.states.LocalPortfolioHistoryState
import rs.raf.jun.presentation.view.states.LocalPurchasedStockState
import rs.raf.jun.presentation.view.states.LocalUserState
import rs.raf.jun.presentation.viewmodel.MainViewModel
import java.text.SimpleDateFormat

class PortfolioFragment : Fragment(R.layout.fragment_portfolio){

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()
    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!
    private var portfolioHistoryList: List<PortfolioHistory>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initObservers()
        initUi()
    }

    private fun initObservers() {
        mainViewModel.localPortfolioHistoryState.observe(viewLifecycleOwner){
            renderPortfolioHistoryState(it)
        }
        val pref = context?.getSharedPreferences(requireContext().packageName, Activity.MODE_PRIVATE)
        val email = pref?.getString(LoginActivity.SharedPrefKeys.EMAIL_KEY, null)

        mainViewModel.getAllPortfolioHistoryNodesByUserEmail(email!!)
    }

    private fun renderPortfolioHistoryState(state: LocalPortfolioHistoryState?) {
        when(state){
            is LocalPortfolioHistoryState.GotAllHistory -> {
                portfolioHistoryList = state.portfolioHistoryList
                generateChart()
            }
            is LocalPortfolioHistoryState.Error -> {
                Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateChart() {

        val barsWithDate = ArrayList<BarWithDate>()
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

        for(bar in portfolioHistoryList!!){
            val barWithDate = BarWithDate(bar.value, formatter.parse(bar.time))
            barsWithDate.add(barWithDate)
        }
        barsWithDate.sortedBy { bar -> bar.time }

        val entries = ArrayList<BarEntry>()
        var cnt = 0
        for (bar in barsWithDate){
            entries.add(BarEntry(cnt.toFloat(), bar.price))
            cnt++
        }

        val barDataSet = BarDataSet(entries, "Portfolio value")
        val barData = BarData()
        barData.addDataSet(barDataSet)

        binding.portfolioHistoryChart.data = barData
        binding.portfolioHistoryChart.invalidate()
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        val pref = context?.getSharedPreferences(requireContext().packageName, Activity.MODE_PRIVATE)
        val accountBalance = pref?.getFloat(LoginActivity.SharedPrefKeys.ACCOUNT_BALANCE_KEY, 10000f)
        val portofolioValue = pref?.getFloat(LoginActivity.SharedPrefKeys.PORTFOLIO_VALUE_KEY, 0f)
        binding.accountBalanceTv.text = "Account balance: " + accountBalance.toString()
        binding.portfolioValueTv.text = "Portfolio value: " + portofolioValue.toString()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

}