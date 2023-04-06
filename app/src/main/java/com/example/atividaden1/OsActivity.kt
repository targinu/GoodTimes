package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityOsBinding


class OsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Ir para a tela de cadastro de Ordem de serviço
        binding.btnCadastrarOs.setOnClickListener{
            val intent = Intent(this,CadastroOsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //VOLTAR PARA A TELA INICIAL
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}