package my.com.fauzan.advisoryapps.viewmodel

import androidx.lifecycle.ViewModel
import my.com.fauzan.advisoryapps.model.Model
import my.com.fauzan.advisoryapps.service.repository.DashboardRepository

class DashboardViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {

    fun fetchData() {
        dashboardRepository.hitListing()
    }

    fun updateData(user: Model.Person) {
        dashboardRepository.hitUpdateData(user)
    }

    fun getListingData() = dashboardRepository.getListingData()
    fun getUpdateData() = dashboardRepository.getUpdateData()


}