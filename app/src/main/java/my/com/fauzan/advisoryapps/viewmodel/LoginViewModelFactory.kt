package my.com.fauzan.advisoryapps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.com.fauzan.advisoryapps.service.repository.LoginRepository

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory (private val loginRepository: LoginRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginRepository) as T
    }
}