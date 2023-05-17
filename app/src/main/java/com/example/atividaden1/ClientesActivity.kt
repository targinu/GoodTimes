package com.example.atividaden1

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.atividaden1.databinding.ActivityClientesBinding
import com.google.firebase.database.*

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var clienteRef: DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //Adiciona um listener que exibe os dados na tela
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpa as linhas da tabela
                tableLayout.removeAllViews()

                //Preenche a tabela com os dados dos clientes
                for (clienteSnapshot in snapshot.children) {

                    //Cria uma nova linha na tabela
                    val tableRow = TableRow(this@ClientesActivity)
                    tableRow.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )

                    //Cria as colunas da tabela com os dados dos clientes

                    //Exibe o nome do Cliente na tela
                    val nomeTextView = TextView(this@ClientesActivity)
                    val nome = clienteSnapshot.child("nome").getValue(String::class.java)
                    if (nome != null) {
                        if (nome.length > 10) {
                            nomeTextView.text = nome.substring(0, 10) + "..."
                        } else {
                            nomeTextView.text = nome
                        }
                    }
                    nomeTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(nomeTextView)

                    val codigoTextView = TextView(this@ClientesActivity)
                    val codigo = clienteSnapshot.child("codigo").getValue(String::class.java)
                    if (codigo != null) {
                        if (codigo.length > 2) {
                            codigoTextView.text = codigo.substring(0, 2) + "..."
                        } else {
                            codigoTextView.text = codigo
                        }
                    }
                    codigoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(codigoTextView)

                    val orcamentoTextView = TextView(this@ClientesActivity)
                    val orcamento = clienteSnapshot.child("orcamento").getValue(Float::class.java)
                    if (orcamento != null) {
                        val orcamentoString = orcamento.toString()
                        if (orcamentoString.length > 7) {
                            orcamentoTextView.text = orcamentoString.substring(0, 7) + "..."
                        } else {
                            orcamentoTextView.text = orcamentoString
                        }
                    }
                    orcamentoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(orcamentoTextView)

                    //Exclusão do cliente do banco
                    val excluirImageView = ImageView(this@ClientesActivity)
                    excluirImageView.setImageResource(R.drawable.ic_delete_foreground)
                    excluirImageView.setPadding(8, 0, 8, 50)
                    excluirImageView.setOnClickListener {
                        //Remove o cliente correspondente no banco de dados
                        val clienteRef = databaseRef.child(clienteSnapshot.key!!)
                        clienteRef.removeValue()
                    }
                    tableRow.addView(excluirImageView)

                    //Edição de clientes
                    //Adiciona um OnClickListener para cada linha da tabela
                    tableRow.setOnClickListener {
                        //Cria um Intent para abrir a Activity de edição
                        val intent = Intent(this@ClientesActivity, CadastroClienteActivity::class.java)
                        //Transfere os dados do cliente para a pagina de criação para poder editar
                        val bundle = Bundle().apply {
                            putString("nome", nome)
                            putString("codigo", codigo)
                            putFloat("orcamento", orcamento!!)
                        }
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    //Adiciona os dados na tabela
                    tableLayout.addView(tableRow)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Falha ao carregar clientes.", error.toException())
            }
        })

        //VOLTAR PARA A TELA INICIAL
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //IR PARA A TELA DE CADASTRO DE CLIENTES
        binding.btnCadastrarCliente.setOnClickListener{
            val intent = Intent(this,CadastroClienteActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}


