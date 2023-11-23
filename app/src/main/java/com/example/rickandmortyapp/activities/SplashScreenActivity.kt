package com.example.rickandmortyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.rickandmortyapp.databinding.ActivitySplashScreenBinding
import com.example.rickandmortyapp.viewModels.SplashViewModel

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashViewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashViewModel= ViewModelProvider(this)[SplashViewModel::class.java]

        observeViewModel()
    }


    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            splashViewModel.navigateToNextScreen.collect { navigate ->
                if (navigate) {
                    navigateToMainScreen()
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}