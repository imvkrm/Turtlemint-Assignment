package com.vikram.turtlemintassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}