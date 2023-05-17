package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityCadastrarClienteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastroClienteActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCadastrarClienteBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val nome = bundle?.getString("nome")
        val codigo = bundle?.getString("codigo")
        val orcamento = intent.getFloatExtra("orcamento", 0.0f)

        //Utiliza os dados para preencher os campos de edição
        binding.editTextNome.setText(nome)
        binding.editTextCodigo.setText(codigo)
        binding.editTextOrcamento.setText(orcamento.toString())

        binding.buttonSalvar.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val codigo = binding.editTextCodigo.text.toString()

            //teste
            val orcamento = binding.editTextOrcamento.text.toString().toFloat()

            database = FirebaseDatabase.getInstance().getReference("clientes")
            val Cliente = Cliente(nome,codigo,orcamento)
            database.child(codigo).setValue(Cliente).addOnSuccessListener {

                binding.editTextNome.text.clear()
                binding.editTextCodigo.text.clear()
                binding.editTextOrcamento.text.clear()

                Toast.makeText(this,"Cliente cadastrado com sucesso!",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(this,"Falha ao cadastrar cliente!",Toast.LENGTH_SHORT).show()
            }
        }

        //Voltar para a pagina anterior
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,ClientesActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
