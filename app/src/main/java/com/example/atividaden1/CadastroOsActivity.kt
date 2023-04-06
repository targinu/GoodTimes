package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityCadastrarOsBinding

class CadastroOsActivity: AppCompatActivity(){

    private lateinit var binding: ActivityCadastrarOsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarOsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Voltar para ordem de serviço
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}