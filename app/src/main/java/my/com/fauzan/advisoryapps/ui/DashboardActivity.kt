package my.com.fauzan.advisoryapps.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import my.com.fauzan.advisoryapps.App.Companion.prefs
import my.com.fauzan.advisoryapps.utils.Constants
import my.com.fauzan.advisoryapps.adapter.PaginationScrollListener
import my.com.fauzan.advisoryapps.R
import my.com.fauzan.advisoryapps.adapter.UserListAdapter
import my.com.fauzan.advisoryapps.model.Model
import my.com.fauzan.advisoryapps.utils.CustomDialog
import my.com.fauzan.advisoryapps.utils.InjectorUtils
import my.com.fauzan.advisoryapps.viewmodel.DashboardViewModel


class DashboardActivity : AppCompatActivity(), UserListAdapter.OnItemClickListener {

    private val Tag = DashboardActivity::class.java.simpleName
    private lateinit var progressDialog: ProgressDialog

    private lateinit var dashboardViewModel: DashboardViewModel
    private val usersAdapter =
        UserListAdapter(arrayListOf(), this)
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private val userList = ArrayList<Model.Person>()

    private var totalItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val factory = InjectorUtils.provideDashboardViewModelFactory()
        dashboardViewModel =
            ViewModelProviders.of(this, factory).get(DashboardViewModel::class.java)
        dashboardViewModel.fetchData()

        initView()
    }

    private fun initView() {
        progressDialog = ProgressDialog(this)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            //Reset
            userList.clear()
            usersAdapter.updateUsers(userList)
            dashboardViewModel.fetchData()
        }

        btn_logout.setOnClickListener {
            // Clear data
            prefs?.userData = null
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        rv_users.apply {
            val layoutManager = LinearLayoutManager(context)
            setLayoutManager(layoutManager)
            adapter = usersAdapter
            setOnScrollListener(object : PaginationScrollListener(layoutManager) {
                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = false
                    if (userList.size < totalItems)
                        dashboardViewModel.fetchData()
                    else
                        Toast.makeText(context, "No more data", Toast.LENGTH_SHORT).show()
                }

            })
        }


        observeViewModel()
    }

    private fun observeViewModel() {
        dashboardViewModel.getListingData().observe(this, Observer {
            it?.let {
                when (it.callStatus) {
                    Constants.Progress.LOADING -> {
                        loading_view.visibility = View.VISIBLE
                    }
                    Constants.Progress.SUCCESSFUL -> {
                        loading_view.visibility = View.GONE
                        if (it.status?.code == 200) {
                            totalItems = it.list.size
                            Log.e(Tag, "Total items: $totalItems")
                            Log.e(Tag, "Display items: ${userList.size}")

                            //  Implement pagination
                            // Display first 5 items
                            for (i in userList.size..userList.size + 4) {
                                if (i < it.list.size) userList.add(it.list[i]) else break
                            }
                            usersAdapter.updateUsers(userList)
                        } else {
                            Toast.makeText(this, it.status?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        loading_view.visibility = View.GONE
                        Toast.makeText(this, it.status?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        dashboardViewModel.getUpdateData().observe(this, Observer {
            it?.let {
                when (it.callStatus) {
                    Constants.Progress.LOADING -> {
                        progressDialog.setMessage("Updating...")
                        progressDialog.show()
                    }
                    Constants.Progress.SUCCESSFUL -> {
                        progressDialog.dismiss()
                        if (it.status?.code == 200) {
                            //Refresh data
                            userList.clear()
                            dashboardViewModel.fetchData()
                            Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, it.status?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        progressDialog.dismiss()
                        Toast.makeText(this, it.status?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onItemClicked(user: Model.Person) {
        Toast.makeText(
            this,
            "Id: ${user.id} | Name: ${user.name} | Distance: ${user.distance}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onItemLongClicked(user: Model.Person) {
        val dialog = CustomDialog(this, user)
        dialog.setOnClickListener(View.OnClickListener {
            val updateData = dialog.getUpdatedUser()
            if (updateData != null) {
                dashboardViewModel.updateData(updateData)
                dialog.dismiss()
            }
        })
        dialog.show()
    }
}
