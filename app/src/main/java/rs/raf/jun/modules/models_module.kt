package rs.raf.jun.modules
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.jun.data.datasources.local.MyDataBase
import rs.raf.jun.data.datasources.remote.mock.news.MockNewsService
import rs.raf.jun.data.datasources.remote.mock.stock.MockStockService
import rs.raf.jun.data.repositories.*
import rs.raf.jun.presentation.viewmodel.MainViewModel

val modelsModule = module{

    viewModel { MainViewModel(newsRepository = get(), userRepository = get(), stockRepository = get(), portfolioHistoryRepository = get()) }

    single<NewsRepository> {NewsRepositoryImpl(remoteDataSource = get())}

    //single<NewsService> { create(retrofit = get()) }
    single<MockNewsService> { createMockNewsService() }

    single<UserRepository> { UserRepositoryImpl(localDataSource = get()) }

    single { get<MyDataBase>().getUserDao() }

    single<StockRepository> { StockRepositoryImpl(localDataSource = get(), remoteDataSource = get()) }

    single { get<MyDataBase>().getPurchasedStockDao() }

    //single<StockService> { create(retrofit = get()) }
    single<MockStockService> { createMockStockService() }

    single<PortfolioHistoryRepository> { PortfolioHistoryRepositoryImpl(localDataSource = get()) }

    single { get<MyDataBase>().getPortfolioHistoryDao() }

}
