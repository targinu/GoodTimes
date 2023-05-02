package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //Ir para a página de clientes
        binding.btnGerenciarCliente.setOnClickListener{
            val intent = Intent(this,ClientesActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Ir para a página de ordem de serviço
        binding.btnGerenciarOS.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Ir para a página de sobre da empresa
        binding.logo.setOnClickListener{
            val intent = Intent(this,SobreActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.txtGoodTimes.setOnClickListener{
            val intent = Intent(this,SobreActivity::class.java)
            startActivity(intent)
            finish()
        }

        //DESLOGAR USUÁRIO
        binding.deslogar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}