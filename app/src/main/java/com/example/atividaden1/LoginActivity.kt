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
    private var userLoggedOutInAnotherDevice = false

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

        //Adiciona o Auth State Listener
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null && !userLoggedOutInAnotherDevice) {
                //O usuário foi desautenticado em outro dispositivo, desloga o usuário desta sessão
                //Toast.makeText(this, "Você foi deslogado em outro dispositivo!", Toast.LENGTH_SHORT).show()
                userLoggedOutInAnotherDevice = true
                firebaseAuth.signOut()
            }
            else if (user != null && userLoggedOutInAnotherDevice) {
                //Um usuário foi autenticado em outro dispositivo, desloga o usuário desta sessão
                //Toast.makeText(this, "Você foi logado em outro dispositivo!", Toast.LENGTH_SHORT).show()
                userLoggedOutInAnotherDevice = true
                FirebaseAuth.getInstance().signOut() //adiciona esta linha para desconectar o usuário
            }
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

        //Ir para a pagina sobre
        binding.sobre.setOnClickListener{
            val intent = Intent(this,SobreActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        userLoggedOutInAnotherDevice = false

        firebaseAuth.addAuthStateListener(authStateListener)

        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            Toast.makeText(this,"Bem vindo de volta!", Toast.LENGTH_SHORT).show()
            telaUser()
        } else {
            //Toast.makeText(this,"Faça login para prosseguir!", Toast.LENGTH_SHORT).show()
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