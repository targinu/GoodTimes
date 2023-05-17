package com.example.atividaden1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.atividaden1.databinding.ActivityOsEncerradasBinding
import com.google.firebase.database.*

class OsEncerradasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOsEncerradasBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var ordemRef: DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOsEncerradasBinding.inflate(layoutInflater)
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
                    // Exibe todos os itens
                    for (i in 0 until tableLayout.childCount) {
                        val row = tableLayout.getChildAt(i) as TableRow
                        row.visibility = View.VISIBLE
                    }
                } else {
                    // Filtra os itens
                    for (i in 0 until tableLayout.childCount) {
                        val row = tableLayout.getChildAt(i) as TableRow
                        val cliente = (row.getChildAt(0) as TextView).text.toString()
                        val servico = (row.getChildAt(1) as TextView).text.toString()
                        val id = (row.getChildAt(2) as TextView).text.toString()

                        if (!cliente.contains(newText, true) && !servico.contains(newText, true) && !id.contains(newText, true)) {
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
                    val encerrada = ordemSnapshot.child("encerrada").getValue(Boolean::class.java)
                    val cancelada = ordemSnapshot.child("cancelada").getValue(Boolean::class.java)

                    //A ordem de serviço está encerrada, então a exibe na tabela
                    if (encerrada == true) {
                        // Cria uma nova linha na tabela
                        val tableRow = TableRow(this@OsEncerradasActivity)
                        tableRow.layoutParams = TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT
                        )

                        //Exibe os dados na tabela
                        //Exibe o nome do Cliente na tela
                        val nomeClienteTextView = TextView(this@OsEncerradasActivity)
                        val nomeCliente = ordemSnapshot.child("cliente").child("nome")
                            .getValue(String::class.java)
                        if (nomeCliente != null) {
                            if (nomeCliente.length > 8) {
                                nomeClienteTextView.text = nomeCliente.substring(0, 8) + "..."
                            } else {
                                nomeClienteTextView.text = nomeCliente
                            }
                        }
                        nomeClienteTextView.setPadding(8, 0, 8, 50)
                        tableRow.addView(nomeClienteTextView)

                        //Exibe o nome do serviço na tela
                        val nomeServicoTextView = TextView(this@OsEncerradasActivity)
                        val nomeServico =
                            ordemSnapshot.child("nomeServico").getValue(String::class.java)
                        if (nomeServico != null) {
                            if (nomeServico.length > 10) {
                                nomeServicoTextView.text = nomeServico.substring(0, 10) + "..."
                            } else {
                                nomeServicoTextView.text = nomeServico
                            }
                        }
                        nomeServicoTextView.setPadding(8, 0, 8, 50)
                        tableRow.addView(nomeServicoTextView)

                        //Exibe o ID na tela
                        val idTextView = TextView(this@OsEncerradasActivity)
                        val id = ordemSnapshot.child("id").getValue(String::class.java)
                        if (id != null) {
                            if (id.length > 2) {
                                idTextView.text = id.substring(0, 2) + "..."
                            } else {
                                idTextView.text = id
                            }
                        }
                        idTextView.setPadding(8, 0, 8, 50)
                        tableRow.addView(idTextView)

                        //Exibe o preço na tela
                        val precoTextView = TextView(this@OsEncerradasActivity)
                        val preco = ordemSnapshot.child("preco").getValue(Float::class.java)
                        if (preco != null) {
                            val precoString = preco.toString()
                            if (precoString.length > 4) {
                                precoTextView.text = precoString.substring(0, 4) + "."
                            } else {
                                precoTextView.text = precoString
                            }
                        }
                        precoTextView.setPadding(8, 0, 8, 50)
                        tableRow.addView(precoTextView)

                        //Exibe o valor de desconto na tela
                        val descontoTextView = TextView(this@OsEncerradasActivity)
                        val desconto = ordemSnapshot.child("desconto").getValue(Float::class.java)
                        if (desconto != null) {
                            val descontoString = desconto.toString()
                            if (descontoString.length > 4) {
                                descontoTextView.text = descontoString.substring(0, 4) + "."
                            } else {
                                descontoTextView.text = descontoString
                            }
                        }
                        descontoTextView.setPadding(8, 0, 8, 50)
                        tableRow.addView(descontoTextView)

                        //Exibe o valor total na tela
                        val totalTextView = TextView(this@OsEncerradasActivity)
                        val total = ordemSnapshot.child("total").getValue(Float::class.java)
                        if (total != null) {
                            val totalString = total.toString()
                            if (totalString.length > 4) {
                                totalTextView.text = totalString.substring(0, 4) + "."
                            } else {
                                totalTextView.text = totalString
                            }
                        }
                        totalTextView.setPadding(8, 0, 8, 50)
                        tableRow.addView(totalTextView)

                        //Ir para tela de Comentarios
                        //Adiciona um OnClickListener para cada linha da tabela
                        tableRow.setOnClickListener {
                            //Cria um Intent para abrir a Activity de edição
                            val intent = Intent(this@OsEncerradasActivity, ComentariosActivity::class.java)
                            //Transfere os dados do cliente para a pagina de criação para poder editar
                            val bundle = Bundle().apply {
                                putString("nomeCliente", nomeCliente)
                                putString("nomeServico", nomeServico)
                                putString("id", id)
                                putFloat("total", total!!)
                            }
                            intent.putExtras(bundle)
                            startActivity(intent)
                            finish()
                        }

                        //Adiciona a nova linha na tabela
                        tableLayout.addView(tableRow)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Falha ao carregar ordens de serviço.", error.toException())
            }
        })

        //VOLTAR PARA A TELA ANTERIOR
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}