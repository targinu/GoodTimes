package com.example.atividaden1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.atividaden1.databinding.ActivityClientesBinding
import com.google.firebase.auth.FirebaseAuth

class ClientesActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityClientesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //VOLTAR PARA A TELA INICIAL
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //IR PARA A TELA DE CADASTRO DE CLIENTES
        binding.btnCadastrarCliente.setOnClickListener{
            val intent = Intent(this,CadastroClienteActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}


