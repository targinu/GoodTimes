package com.example.atividaden1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityComentariosBinding
import com.google.firebase.database.*

class ComentariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComentariosBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComentariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val nomeServico = bundle?.getString("nomeServico")
        val id = bundle?.getString("id")

        //Utiliza os dados para preencher os campos de edição
        binding.textViewServicoDesc.setText(nomeServico)
        binding.textViewIdDesc.setText(id)

        //Iniciando a tabela
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val databaseRef = FirebaseDatabase.getInstance().getReference("ordens").child(id!!).child("comentarios")

        //Adiciona um listener
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpa as linhas da tabela
                tableLayout.removeAllViews()

                //Preenche a tabela com os dados dos clientes
                for (comentarioSnapshot in snapshot.children) {
                    val assunto = comentarioSnapshot.child("assunto").getValue(String::class.java)

                    //Cria uma nova linha na tabela
                    val tableRow = TableRow(this@ComentariosActivity)
                    tableRow.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )

                    //Cria as colunas da tabela com os dados dos clientes
                    val assuntoTextView = TextView(this@ComentariosActivity)
                    assuntoTextView.text = assunto
                    assuntoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(assuntoTextView)

                    //Limita o tanto de caracteres exibidos na tela
                    val comentarioTextView = TextView(this@ComentariosActivity)
                    val comentario = comentarioSnapshot.child("comentario").getValue(String::class.java)
                    if (comentario != null) {
                        if (comentario.length > 25) {
                            comentarioTextView.text = comentario.substring(0, 25) + "..."
                        } else {
                            comentarioTextView.text = comentario
                        }
                    }
                    comentarioTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(comentarioTextView)

                    /** acho que não é interessante ter função de exclusão aqui
                    //Exclusão do comentario do banco
                    val excluirImageView = ImageView(this@ComentariosActivity)
                    excluirImageView.setImageResource(R.drawable.ic_delete_foreground)
                    excluirImageView.setPadding(8, 0, 8, 50)
                    excluirImageView.setOnClickListener {
                        //Remove o comentario correspondente no banco de dados
                        val comentariosRef = databaseRef.child(clienteSnapshot.key!!)
                        comentariosRef.removeValue()
                    }
                    tableRow.addView(excluirImageView)
                    **/


                    //Visualização de comentario
                    //Adiciona um OnClickListener para cada linha da tabela
                    tableRow.setOnClickListener {
                        //Cria um Intent para abrir a Activity de edição
                        val intent = Intent(this@ComentariosActivity, visualizarComentarioActivity::class.java)
                        //Transfere os dados do cliente para a pagina de criação para poder editar
                        val bundle = Bundle().apply {
                            putString("assunto", assunto)
                            putString("comentario", comentario)
                        }
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    //Adiciona os dados na tabela
                    tableLayout.addView(tableRow)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Falha ao carregar clientes.", error.toException())
            }
        })

        //IR PARA A TELA DE CADASTRO DE COMENTARIO
        binding.btnCadastrarComentario.setOnClickListener{
            val intent = Intent(this,ComentarActivity::class.java)
            val bundle = Bundle().apply {
                putString("id",id)
            }
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

        //VOLTAR PARA A TELA ANTERIOR
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}