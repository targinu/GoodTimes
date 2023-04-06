package com.example.atividaden1

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityCadastrarOsBinding
import com.google.firebase.database.*

class CadastroOsActivity: AppCompatActivity(){

    private lateinit var binding: ActivityCadastrarOsBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var clienteRef: DatabaseReference
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarOsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        //Iniciando a tabela
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val databaseRef = FirebaseDatabase.getInstance().getReference("clientes")

        //Fazendo pesquisa dos clientes
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    //Exibe todos os itens
                    for (i in 0 until tableLayout.childCount) {
                        val row = tableLayout.getChildAt(i) as TableRow
                        row.visibility = View.VISIBLE
                    }
                } else {
                    //Filtra os itens
                    for (i in 0 until tableLayout.childCount) {
                        val row = tableLayout.getChildAt(i) as TableRow
                        val codigo = (row.getChildAt(0) as TextView).text.toString()
                        val nome = (row.getChildAt(1) as TextView).text.toString()

                        if (!codigo.contains(newText, true) && !nome.contains(newText, true)) {
                            row.visibility = View.GONE
                        } else {
                            row.visibility = View.VISIBLE
                        }
                    }
                }
                return true
            }
        })

        //Adiciona um listener
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpa as linhas da tabela
                tableLayout.removeAllViews()

                //Preenche a tabela com os dados dos clientes
                for (clienteSnapshot in snapshot.children) {
                    val nome = clienteSnapshot.child("nome").getValue(String::class.java)
                    val codigo = clienteSnapshot.child("codigo").getValue(String::class.java)
                    val orcamento = clienteSnapshot.child("orcamento").getValue(Float::class.java)

                    //Cria uma nova linha na tabela
                    val tableRow = TableRow(this@CadastroOsActivity)
                    tableRow.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )

                    //Cria as colunas da tabela com os dados dos clientes
                    val nomeTextView = TextView(this@CadastroOsActivity)
                    nomeTextView.text = nome
                    nomeTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(nomeTextView)

                    val codigoTextView = TextView(this@CadastroOsActivity)
                    codigoTextView.text = codigo
                    codigoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(codigoTextView)

                    val orcamentoTextView = TextView(this@CadastroOsActivity)
                    orcamentoTextView.text = orcamento?.toFloat().toString()
                    orcamentoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(orcamentoTextView)

                    //Adiciona os dados na tabela
                    tableLayout.addView(tableRow)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "Falha ao carregar clientes.", error.toException())
            }
        })
        //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

        //Voltar para ordem de serviço
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}