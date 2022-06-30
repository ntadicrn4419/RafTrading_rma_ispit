package rs.raf.jun.presentation.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rs.raf.jun.data.models.stocks.SimpleStock
import rs.raf.jun.data.models.user.PortfolioHistory
import rs.raf.jun.data.models.user.User
import rs.raf.jun.data.repositories.NewsRepository
import rs.raf.jun.data.repositories.PortfolioHistoryRepository
import rs.raf.jun.data.repositories.StockRepository
import rs.raf.jun.data.repositories.UserRepository
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.states.*
import timber.log.Timber

class MainViewModel(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository,
    private val stockRepository: StockRepository,
    private val portfolioHistoryRepository: PortfolioHistoryRepository

) : ViewModel(), MainContract.ViewModel {

    private val subscriptions = CompositeDisposable()
    override val remoteNewsState: MutableLiveData<RemoteNewsState> = MutableLiveData()
    override val localUserState: MutableLiveData<LocalUserState> = MutableLiveData()
    override val remoteStockState: MutableLiveData<RemoteStockState> = MutableLiveData()
    override val localPurchasedStockState: MutableLiveData<LocalPurchasedStockState> = MutableLiveData()
    override val localPortfolioHistoryState: MutableLiveData<LocalPortfolioHistoryState> = MutableLiveData()

    override fun addUser(user: User) {
        val subscription = userRepository
            .insert(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localUserState.value = LocalUserState.AddedUser("Successfully added user")
                },
                {
                    localUserState.value = LocalUserState.ErrorState("Error happened while adding user in the db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllUsers() {
        val subscription = userRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localUserState.value = LocalUserState.GotAllUsers(it)
                },
                {
                    localUserState.value = LocalUserState.ErrorState("Error happened while getting data about users from the db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun updateUserPortfolio(userEmail: String, accountBalance: Float, portfolioValue: Float) {
        val subscription = userRepository
            .update(userEmail, accountBalance, portfolioValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localUserState.value = LocalUserState.UpdatedUserData("Updated user portfolio")
                },
                {
                    localUserState.value = LocalUserState.ErrorState("Error happened while updating user portfolio")
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchStocks() {
        val subscription = stockRepository
            .fetchAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    remoteStockState.value = RemoteStockState.SuccessRemoteState(it)
                },
                {
                    remoteStockState.value = RemoteStockState.ErrorRemoteState("Error happened while fetching data about stocks from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun addStock(simpleStock: SimpleStock) {
        val subscription = stockRepository
            .insert(simpleStock)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.AddedStockLocalState("Added stock in db")
                },
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.ErrorLocalState("Error happened while adding stock in db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getStockBySymbol(symbol: String) {
        val subscription = stockRepository
            .fetchStockBySymbol(symbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    remoteStockState.value = RemoteStockState.SuccessDetailRemoteState(it)
                },
                {
                    remoteStockState.value = RemoteStockState.ErrorRemoteState("Error happened while fetching data about stock you have chosen from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }


    override fun getAllStocksByUserEmail(userEmail: String) {
        val subscription = stockRepository
            .getAllByUserEmail(userEmail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.SuccessLocalState(it)
                },
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.ErrorLocalState("Error happened while getting data about purchesd stocks from the db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun deleteStock(simpleStock: SimpleStock, numberOfSharesToDelete: Int) {
        val subscription = stockRepository
            .delete(simpleStock, numberOfSharesToDelete)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.DeletedStockLocalState("Deleted stock from db")
                },
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.ErrorLocalState("Error while deleting stock from db")
                }
            )
        subscriptions.add(subscription)
    }

    override fun deleteAllStocks(simpleStock: SimpleStock) {
        val subscription = stockRepository
            .deleteAll(simpleStock)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.DeletedStockLocalState("Deleted all stocks from db")
                },
                {
                    localPurchasedStockState.value = LocalPurchasedStockState.ErrorLocalState("Error while deleting stocks from db")
                }
            )
        subscriptions.add(subscription)
    }

    override fun insertPortfolioHistoryNode(userEmail: String, portfolioHistory: PortfolioHistory) {
        val subscription = portfolioHistoryRepository
            .insert(userEmail, portfolioHistory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localPortfolioHistoryState.value = LocalPortfolioHistoryState.AddedInHistory("Added in portfolio history")
                },
                {
                    localPortfolioHistoryState.value = LocalPortfolioHistoryState.Error("Error while adding in portfolio history")
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllPortfolioHistoryNodesByUserEmail(userEmail: String) {
        val subscription = portfolioHistoryRepository
            .getAllByUserEmail(userEmail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    localPortfolioHistoryState.value = LocalPortfolioHistoryState.GotAllHistory(it)
                },
                {
                    localPortfolioHistoryState.value = LocalPortfolioHistoryState.Error("Error happened while getting portfolio history data from the db")
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchNews() {
        val subscription = newsRepository
            .fetchAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    remoteNewsState.value = RemoteNewsState.SuccessRemoteState(it)
                },
                {
                    remoteNewsState.value = RemoteNewsState.ErrorRemoteState("Error happened while fetching data from the server")
                }
            )
        subscriptions.add(subscription)
    }

    override fun onCleared() {
        subscriptions.clear()
        super.onCleared()
    }

}