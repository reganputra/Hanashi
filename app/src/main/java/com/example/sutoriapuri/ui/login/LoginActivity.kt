package com.example.sutoriapuri.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sutoriapuri.MainActivity
import com.example.sutoriapuri.R
import com.example.sutoriapuri.data.Result
import com.example.sutoriapuri.data.ViewModelFactory
import com.example.sutoriapuri.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLogin()
        showLoading(false)
    }

    private fun userLogin(){
        binding.buttonLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.userLogin(email, password).observe(this){result ->
                    if (result!=null){
                        when(result){
                            is Result.Loading -> showLoading(true)

                            is Result.Success ->{
                                showLoading(false)
                                AlertDialog.Builder(this).apply {
                                    // Login Action
                                    setTitle("Oh Yeah!")
                                    setMessage("Berhasil Login!!")
                                    setPositiveButton("Next") { _, _ ->
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                //Error
                                showLoading(false)
                                Toast.makeText(this, "Login Gagal: ${result.error}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}