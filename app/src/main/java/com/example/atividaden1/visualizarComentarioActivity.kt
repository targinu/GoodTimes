package com.example.atividaden1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityVisualizarComentarioBinding
import com.google.firebase.database.DatabaseReference

class visualizarComentarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisualizarComentarioBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizarComentarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val assunto = bundle?.getString("assunto")
        val comentario = bundle?.getString("comentario")

        //Utiliza os dados para preencher os campos de edição
        binding.textViewAssuntoDesc.setText(assunto)
        binding.textViewComentarioDesc.setText(comentario)

        //VOLTAR PARA A TELA ANTERIOR
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,ComentariosActivity::class.java)
            /**Não crasha sem mas talvez precise
            intent.putExtra("assunto", assunto)
            intent.putExtra("comentario", comentario)
            **/
            startActivity(intent)
            finish()
        }
    }
}