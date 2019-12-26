package my.com.fauzan.advisoryapps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.com.fauzan.advisoryapps.service.repository.DashboardRepository

@Suppress("UNCHECKED_CAST")
class DashboardViewModelFactory (private val dashboardRepository: DashboardRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(dashboardRepository) as T
    }
}