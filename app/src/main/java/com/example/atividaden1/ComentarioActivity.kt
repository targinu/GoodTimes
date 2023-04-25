package com.example.atividaden1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityComentarioBinding

class ComentarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComentarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComentarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}