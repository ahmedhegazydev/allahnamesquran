package app.asmaquran.mobile

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AsmaQuranApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AsmaQuranApp)
            modules(appModule)
        }
    }
}