package my.com.fauzan.advisoryapps.utils

import my.com.fauzan.advisoryapps.service.ApiService
import my.com.fauzan.advisoryapps.service.repository.DashboardRepository
import my.com.fauzan.advisoryapps.service.repository.LoginRepository
import my.com.fauzan.advisoryapps.viewmodel.DashboardViewModelFactory
import my.com.fauzan.advisoryapps.viewmodel.LoginViewModelFactory

object InjectorUtils {

    fun provideLoginViewModelFactory(): LoginViewModelFactory {
        val loginRepository = LoginRepository.getInstance(ApiService.create())
        return LoginViewModelFactory(loginRepository)
    }

    fun provideDashboardViewModelFactory(): DashboardViewModelFactory {
        val dashboardRepository = DashboardRepository.getInstance(ApiService.create())
        return DashboardViewModelFactory(dashboardRepository)
    }
}