package rs.raf.jun.presentation.view.fragments
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.jun.R
import rs.raf.jun.data.models.news.News
import rs.raf.jun.data.models.stocks.Stock
import rs.raf.jun.databinding.FragmentDiscoverBinding
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.activities.StockDetailActivity
import rs.raf.jun.presentation.view.recycler.adapters.NewsAdapter
import rs.raf.jun.presentation.view.recycler.adapters.StockAdapter
import rs.raf.jun.presentation.view.states.RemoteNewsState
import rs.raf.jun.presentation.view.states.RemoteStockState
import rs.raf.jun.presentation.viewmodel.MainViewModel


class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var stockAdapter: StockAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
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
       mainViewModel.remoteNewsState.observe(viewLifecycleOwner) {
           renderRemoteNewsState(it)
       }
        mainViewModel.fetchNews()

        mainViewModel.remoteStockState.observe(viewLifecycleOwner){
            renderRemoteStockState(it)
        }
        mainViewModel.fetchStocks()
    }

    private fun initUi() {
        initNewsRecycler()
        initStockRecycler()
    }

    private fun initNewsRecycler() {
        binding.newsListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsAdapter = NewsAdapter(::newsCardClickListener)
        binding.newsListRv.adapter = newsAdapter
    }

    private fun initStockRecycler() {
        binding.stockListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        stockAdapter = StockAdapter(::stockCardClickListener)
        binding.stockListRv.adapter = stockAdapter
    }

    private fun newsCardClickListener(news: News){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(news.link)
        startActivity(i)
    }

    private fun stockCardClickListener(stock: Stock){
        val intent = Intent(activity, StockDetailActivity::class.java)
        intent.putExtra("stockSymbol", stock.symbol)
        intent.putExtra("stockName", stock.name)
        activity?.startActivity(intent)
    }

    private fun renderRemoteNewsState(state: RemoteNewsState) {
        when (state){
            is RemoteNewsState.SuccessRemoteState ->{
                newsAdapter.submitList(state.news)
            }
            is RemoteNewsState.ErrorRemoteState ->{
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun renderRemoteStockState(state: RemoteStockState) {
        when (state){
            is RemoteStockState.SuccessRemoteState ->{
                stockAdapter.submitList(state.stocks)
            }
            is RemoteStockState.ErrorRemoteState ->{
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}