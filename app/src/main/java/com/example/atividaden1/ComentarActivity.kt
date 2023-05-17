package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityComentarBinding
import com.google.firebase.database.*


class ComentarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComentarBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComentarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val nomeCliente = bundle?.getString("nomeCliente")
        val nomeServico = bundle?.getString("nomeServico")
        val id = bundle?.getString("id")
        val total = intent.getFloatExtra("total", 0.0f)

        binding.buttonSalvar.setOnClickListener {
            val assunto = binding.editTextAssunto.text.toString()
            val comentario = binding.editTextComentario.text.toString()

            database = FirebaseDatabase.getInstance().getReference("ordens").child(id!!).child("comentarios")
            val Comentario = Comentario(assunto,comentario)
            database.child(assunto).setValue(Comentario).addOnSuccessListener {

                binding.editTextAssunto.text.clear()
                binding.editTextComentario.text.clear()

                Toast.makeText(this,"Comentario realizado com sucesso!", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(this,"Falha ao cadastrar comentario!", Toast.LENGTH_SHORT).show()
            }
        }

        //VOLTAR PARA A TELA ANTERIOR
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,ComentariosActivity::class.java)
            intent.putExtra("nomeCliente", nomeCliente)
            intent.putExtra("nomeServico", nomeServico)
            intent.putExtra("id", id)
            intent.putExtra("total", total!!)
            startActivity(intent)
            finish()
        }
    }

}