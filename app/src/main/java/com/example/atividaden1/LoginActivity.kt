package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.cadastrar.setOnClickListener{
            val intent = Intent(this,CadastroActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.login.setOnClickListener{
            val email = binding.username.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){
                   firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this,"Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                           Toast.makeText(this, "Login e senha estão incorretos", Toast.LENGTH_SHORT).show()
                       }
                    }
            } else {
                Toast.makeText(this,"Os campos deve ser preenchidos!", Toast.LENGTH_SHORT).show()
            }
        }

        //Adiciona o listener para monitorar o estado de autenticação
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                //Usuário não autenticado
                Toast.makeText(this, "Sua sessão foi encerrada em outro dispositivo", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            Toast.makeText(this,"Bem vindo de volta!", Toast.LENGTH_SHORT).show()
            telaUser()
        } else {
            Toast.makeText(this,"Faça login para prosseguir!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun telaUser() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}