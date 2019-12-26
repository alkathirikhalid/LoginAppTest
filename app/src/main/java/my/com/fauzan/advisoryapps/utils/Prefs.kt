package my.com.fauzan.advisoryapps.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context){
    private val PREF_NAME = "my.com.fauzan.advisoryapps"
    val prefs : SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var userData: String?
        get() = prefs.getString(PREF_NAME, null)
        set(value) = prefs.edit().putString(PREF_NAME, value).apply()
}
