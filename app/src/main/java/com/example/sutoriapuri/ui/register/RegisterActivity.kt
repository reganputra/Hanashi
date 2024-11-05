package com.example.sutoriapuri.ui.register

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
import com.example.sutoriapuri.R
import com.example.sutoriapuri.data.Result
import com.example.sutoriapuri.data.ViewModelFactory
import com.example.sutoriapuri.databinding.ActivityRegisterBinding
import com.example.sutoriapuri.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRegistered()
        showLoading(false)
    }

    private fun userRegistered(){
        binding.buttonForRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }else {
                registerViewModel.userRegister(name, email, password).observe(this){ result ->
                    if (result !=null){
                        when(result){
                            is Result.Loading -> showLoading(true)

                            // Register Action
                            is  Result.Success -> {
                                showLoading(false)
                                AlertDialog.Builder(this).apply {
                                    setTitle("Oh Yeah")
                                    setMessage("Kamu berhasil daftar akun, lanjutkan login!!")
                                    setPositiveButton("Next!") { _, _ ->
                                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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
                                // Error
                                showLoading(false)
                                Toast.makeText(this, "Registrasi Gagal: ${result.error}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbRegister.visibility = View.VISIBLE
        } else {
            binding.pbRegister.visibility = View.GONE
        }
    }

}