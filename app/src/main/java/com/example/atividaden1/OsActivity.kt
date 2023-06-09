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
                        val cliente = (row.getChildAt(0) as TextView).text.toString()
                        val servico = (row.getChildAt(1) as TextView).text.toString()
                        val id = (row.getChildAt(2) as TextView).text.toString()

                        if (!cliente.contains(newText, true) && !servico.contains(newText, true) && !id.contains(newText, true)){
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

                    //A ordem de serviço não está encerrada nem cancelada, então a exibe na tabela
                    if (encerrada != true && cancelada != true) {

                        //Cria uma nova linha na tabela
                        val tableRow = TableRow(this@OsActivity)
                        tableRow.layoutParams = TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT
                        )

                        //Cria as colunas da tabela com os dados das ordens
                        //Exibe o nome do Cliente na tela
                        val nomeClienteTextView = TextView(this@OsActivity)
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
                        val nomeServicoTextView = TextView(this@OsActivity)
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
                        val idTextView = TextView(this@OsActivity)
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
                        val precoTextView = TextView(this@OsActivity)
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
                        val descontoTextView = TextView(this@OsActivity)
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
                        val totalTextView = TextView(this@OsActivity)
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

                        /** Não é necessario excluir ordens
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

                        //Ir para tela de Comentarios
                        //Adiciona um OnClickListener para cada linha da tabela
                        tableRow.setOnClickListener {
                            //Cria um Intent para abrir a Activity de edição
                            val intent = Intent(this@OsActivity, ComentariosActivity::class.java)
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

                        //Adiciona os dados na tabela
                        tableLayout.addView(tableRow)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Falha ao carregar ordens de serviço.", error.toException())
            }
        })

        //Ir para a tela de visualização de ordens canceladas
        binding.btnCanceladas.setOnClickListener{
            val intent = Intent(this,OsCanceladasActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Ir para a tela de visualização de ordens canceladas
        binding.btnEncerradas.setOnClickListener{
            val intent = Intent(this,OsEncerradasActivity::class.java)
            startActivity(intent)
            finish()
        }

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