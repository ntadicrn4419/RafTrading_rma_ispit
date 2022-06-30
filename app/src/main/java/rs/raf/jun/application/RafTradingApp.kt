package rs.raf.jun.application
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import rs.raf.jun.modules.coreModule
import rs.raf.jun.modules.modelsModule
import timber.log.Timber

class RafTradingApp :Application(){
    override fun onCreate() {
        super.onCreate()
        context = this
        init()

    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            internal set
    }

    private fun init() {
        initTimber()
        initKoin()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        val modules = listOf(
            coreModule,
            modelsModule
        )
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@RafTradingApp)
            androidFileProperties()
            fragmentFactory()
            modules(modules)
        }
    }
}