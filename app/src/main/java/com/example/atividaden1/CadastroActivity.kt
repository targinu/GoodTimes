package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityCadastrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.cadastrar.setOnClickListener{
            val nome = binding.username.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.senha.text.toString()
            val confirmPass = binding.confSenha.text.toString()




            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if (it.isSuccessful){
                             //Adiciona o nome de usuario no banco de dados
                            database = FirebaseDatabase.getInstance().getReference("users")
                            val User = User(nome,email)
                            database.child(email).setValue(User).addOnSuccessListener {

                            }.addOnFailureListener{
                                Toast.makeText(this,"Falha ao cadastrar usuario!",Toast.LENGTH_SHORT).show()
                            }

                            Toast.makeText(this,"Usuario criado com sucesso!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else
                        {

                            //As duas linhas abaixo devem ser excluídas
//                            Toast.makeText(this, "Não foi possível realizar o cadastro!", Toast.LENGTH_SHORT).show()
//                            Log.e("CadastroActivity", "Erro ao criar usuário", it.exception)

                            Toast.makeText(this,"Não foi possivel realizar o cadastro!", Toast.LENGTH_SHORT).show()
                            binding.username.text.clear()
                            binding.email.text.clear()
                            binding.senha.text.clear()
                            binding.confSenha.text.clear()
                        }
                    }
                }else{
                    Toast.makeText(this,"Senhas não coincidem", Toast.LENGTH_SHORT).show()
                    binding.senha.text.clear()
                    binding.confSenha.text.clear()

                }
            } else {
                Toast.makeText(this,"Todos os campos deve ser preenchidos!", Toast.LENGTH_SHORT).show()
            }
        }

        //Voltar para a pagina anterior
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}