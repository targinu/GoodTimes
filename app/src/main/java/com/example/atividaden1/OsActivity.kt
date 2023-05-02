package com.example.atividaden1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityOsBinding
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class OsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var ordemRef: DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciando a tabela
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val databaseRef = FirebaseDatabase.getInstance().getReference("ordens")

        //Fazendo pesquisa das ordens de serviço
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

                //Preenche a tabela com os dados das ordens de serviço
                for (ordemSnapshot in snapshot.children) {
                    val nomeCliente = ordemSnapshot.child("cliente").child("nome").getValue(String::class.java)
                    val id = ordemSnapshot.child("id").getValue(String::class.java)
                    val preco = ordemSnapshot.child("preco").getValue(Float::class.java)
                    val desconto = ordemSnapshot.child("desconto").getValue(Float::class.java)
                    val total = ordemSnapshot.child("total").getValue(Float::class.java)

                    //Cria uma nova linha na tabela
                    val tableRow = TableRow(this@OsActivity)
                    tableRow.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )

                    //Cria as colunas da tabela com os dados das ordens

                    val nomeClienteTextView = TextView(this@OsActivity)
                    nomeClienteTextView.text = nomeCliente
                    nomeClienteTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(nomeClienteTextView)


                    val nomeServicoTextView = TextView(this@OsActivity)
                    val nomeServico = ordemSnapshot.child("nomeServico").getValue(String::class.java)
                    if (nomeServico != null) {
                        if (nomeServico.length > 10) {
                            nomeServicoTextView.text = nomeServico.substring(0, 10) + "..."
                        } else {
                            nomeServicoTextView.text = nomeServico
                        }
                    }
                    nomeServicoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(nomeServicoTextView)

                    val idTextView = TextView(this@OsActivity)
                    idTextView.text = id
                    idTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(idTextView)

                    val precoTextView = TextView(this@OsActivity)
                    precoTextView.text = preco?.toFloat().toString()
                    precoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(precoTextView)

                    val descontoTextView = TextView(this@OsActivity)
                    descontoTextView.text = desconto?.toFloat().toString()
                    descontoTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(descontoTextView)

                    val totalTextView = TextView(this@OsActivity)
                    totalTextView.text = total?.toFloat().toString()
                    totalTextView.setPadding(8, 0, 8, 50)
                    tableRow.addView(totalTextView)

                    /** acho que não é interessante ter essa opção
                    //Exclusão da ordem do banco
                    val excluirImageView = ImageView(this@OsActivity)
                    excluirImageView.setImageResource(R.drawable.ic_delete_foreground)
                    excluirImageView.setPadding(8, 0, 8, 50)
                    excluirImageView.setOnClickListener {
                        //Remove a ordem correspondente no banco de dados
                        val ordemRef = databaseRef.child(ordemSnapshot.key!!)
                        ordemRef.removeValue()
                    }
                    tableRow.addView(excluirImageView)
                    **/

                    /** usar essa parte para ir para comentarios
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
                    }
                    **/

                    //Adiciona os dados na tabela
                    tableLayout.addView(tableRow)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Falha ao carregar ordens de serviço.", error.toException())
            }
        })

        //Ir para a tela de cadastro de Ordem de serviço
        binding.btnCadastrarOs.setOnClickListener{
            val intent = Intent(this,CadastroOsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //VOLTAR PARA A TELA INICIAL
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}