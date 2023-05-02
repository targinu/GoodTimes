package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityComentariosBinding
import com.google.firebase.database.*

class ComentariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComentariosBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComentariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val nomeServico = bundle?.getString("nomeServico")

        //Utiliza os dados para preencher os campos de edição
        binding.textViewServicoDesc.setText(nomeServico)

        //IR PARA A TELA DE CADASTRO DE COMENTARIO
        binding.btnCadastrarComentario.setOnClickListener{
            val intent = Intent(this,ComentarActivity::class.java)
            startActivity(intent)
            finish()
        }

        //VOLTAR PARA A TELA ANTERIOR
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}