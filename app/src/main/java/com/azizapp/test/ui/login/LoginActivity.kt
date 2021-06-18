package com.azizapp.test.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.azizapp.test.MainActivityNav
import com.azizapp.test.R
import com.azizapp.test.databinding.ActivityLoginBinding
import com.azizapp.test.ui.daftar.DaftarActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_daftar.*
import kotlinx.android.synthetic.main.splash.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    //private lateinit var SaveSharedPreference: SaveSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.apply {
            lifecycleOwner = this@LoginActivity
            viewModel = loginViewModel
        }

        loginViewModel.action.observe(this, Observer { action ->
            when(action) {
                LoginViewModel.ACTION_LOGIN_SUCCESS -> loginSuccess()
                LoginViewModel.ACTION_LOGIN_FAILED -> loginFailed()
                LoginViewModel.ACTION_LOGIN_ERROR -> loginError()
            }

        })

        tv_login_daftar.setOnClickListener{
            val intent = Intent(this, DaftarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginError() {
        val snackbar = Snackbar.make(binding.root,"Login Gagal, Email atau Password salah",Snackbar.LENGTH_SHORT)
        val textView = snackbar.view.findViewById(R.id.snackbar_text) as TextView
        // change Snackbar text color
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.view.setBackgroundColor(Color.parseColor("#F5365C"))
        snackbar.show()
    }

    private fun loginFailed() {
        val snackbar = Snackbar.make(binding.root,"Login Error, server error",Snackbar.LENGTH_SHORT)
        val textView = snackbar.view.findViewById(R.id.snackbar_text) as TextView
        // change Snackbar text color
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.view.setBackgroundColor(Color.parseColor("#F0CE0E"))
        snackbar.show()
    }

    private fun loginSuccess() {
        startActivity(Intent(this,MainActivityNav::class.java))
        //SaveSharedPreference.setEmail(this, tvEmail.text.toString())
    }
}