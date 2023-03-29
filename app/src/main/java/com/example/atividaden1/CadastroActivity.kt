package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityCadastrarBinding
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityCadastrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.cadastrar.setOnClickListener{
            val email = binding.email.text.toString()
            val pass = binding.senha.text.toString()
            val confirmPass = binding.confSenha.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this,"Usuario criado com sucesso!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,"Não foi possivel realizar o cadastro!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Senhas não coincidem", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Os campos deve ser preenchidos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}