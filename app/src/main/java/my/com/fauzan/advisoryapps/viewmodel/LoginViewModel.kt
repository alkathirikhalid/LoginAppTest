package my.com.fauzan.advisoryapps.viewmodel

import androidx.lifecycle.ViewModel
import my.com.fauzan.advisoryapps.service.repository.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun setLogin(email: String, password: String) {
        loginRepository.hitLogin(email, password)
    }

    fun getLogin() = loginRepository.getData()

}