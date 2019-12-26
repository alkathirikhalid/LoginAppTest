package my.com.fauzan.advisoryapps.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import my.com.fauzan.advisoryapps.App.Companion.prefs
import my.com.fauzan.advisoryapps.R
import my.com.fauzan.advisoryapps.model.Model
import my.com.fauzan.advisoryapps.utils.Constants
import my.com.fauzan.advisoryapps.utils.InjectorUtils
import my.com.fauzan.advisoryapps.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (!prefs?.userData.isNullOrEmpty()){
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        val factory = InjectorUtils.provideLoginViewModelFactory()
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

        initView()
    }

    private fun initView(){
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...Please wait.")
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        observeViewModel()
        initEvent()
    }

    private fun initEvent(){
        btn_login.setOnClickListener{
            if (!et_email.text.isNullOrEmpty() && !et_password.text.isNullOrEmpty()){
                loginViewModel.setLogin(et_email.text.toString(), et_password.text.toString())
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun observeViewModel() {
        loginViewModel.getLogin().observe(this, Observer {
            it?.let {
                when(it.callStatus){
                    Constants.Progress.LOADING -> {
                        progressDialog.show()
                    }
                    Constants.Progress.SUCCESSFUL -> {
                        progressDialog.dismiss()
                        if (it.status?.code == 200){
                            val storeData = Model.UserData(it.id, it.token)
                            // Save in shared prefs
                            prefs?.userData = Gson().toJson(storeData)
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
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
}
