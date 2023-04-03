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
                           Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                       }
                    }
            } else {
                Toast.makeText(this,"Os campos deve ser preenchidos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            Toast.makeText(this,"Bem vindo de volta!", Toast.LENGTH_SHORT).show()
            TelaUser()
        } else {
            Toast.makeText(this,"Faça login para prosseguir!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun TelaUser() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}