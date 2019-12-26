package my.com.fauzan.advisoryapps

import android.app.Application
import my.com.fauzan.advisoryapps.utils.Prefs

class App : Application(){

    companion object {
        var prefs: Prefs? = null
    }
    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}