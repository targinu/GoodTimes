package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityVisualizarComentarioBinding
import com.google.firebase.database.*


class visualizarComentarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisualizarComentarioBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizarComentarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializa a referência do banco de dados Firebase
        databaseRef = FirebaseDatabase.getInstance().reference

        //Obtém o "assunto" e "comentario" da activity anterior
        val bundle = intent.extras
        val assunto = bundle?.getString("assunto")
        val comentario = bundle?.getString("comentario")

        val nomeCliente = bundle?.getString("nomeCliente")
        val nomeServico = bundle?.getString("nomeServico")
        val id = bundle?.getString("id")
        val total = intent.getFloatExtra("total", 0.0f)

        //Verifica se os dados estão vazios
        if (assunto.isNullOrEmpty() || comentario.isNullOrEmpty()) {
            Log.e("VisualizarComentario", "Dados inválidos passados para a atividade atual")
            return
        }

        //Utiliza os dados para preencher os campos de edição
        binding.textViewAssuntoDesc.setText(assunto)
        binding.textViewComentarioDesc.setText(comentario)

        //VOLTAR PARA A TELA ANTERIOR
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,ComentariosActivity::class.java)
            intent.putExtra("assunto", assunto)
            intent.putExtra("comentario", comentario)
            intent.putExtra("nomeCliente", nomeCliente)
            intent.putExtra("nomeServico", nomeServico)
            intent.putExtra("id", id)
            intent.putExtra("total", total!!)
            startActivity(intent)
            finish()
        }
    }
    override fun onStop() {
        super.onStop()
        //Encerra a referência do banco de dados Firebase quando a atividade é encerrada
        databaseRef.removeEventListener(databaseListener)
    }

    //Adiciona um bloco try-catch ao usar a referência do banco de dados Firebase
    private val databaseListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            try {
                // Implemente aqui as operações que utilizam a referência do banco de dados
            } catch (e: Exception) {
                Log.e("VisualizarComentario", "Erro ao acessar o banco de dados Firebase", e)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("VisualizarComentario", "Operação cancelada no banco de dados Firebase", databaseError.toException())
        }
    }
}