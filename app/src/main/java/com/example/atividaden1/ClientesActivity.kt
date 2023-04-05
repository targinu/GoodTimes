package com.example.atividaden1

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.atividaden1.databinding.ActivityClientesBinding
import com.google.firebase.database.*

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var clienteRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val databaseRef = FirebaseDatabase.getInstance().getReference("clientes")

        //Adiciona um listener
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpa as linhas da tabela
                tableLayout.removeAllViews()

                //Preenche a tabela com os dados dos clientes
                for (clienteSnapshot in snapshot.children) {
                    val nome = clienteSnapshot.child("nome").getValue(String::class.java)
                    val codigo = clienteSnapshot.child("codigo").getValue(String::class.java)
                    val orcamento = clienteSnapshot.child("orcamento").getValue(String::class.java)

                    val tableRow = TableRow(this@ClientesActivity)
                    tableRow.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )

                    val nomeTextView = TextView(this@ClientesActivity)
                    nomeTextView.text = nome
                    nomeTextView.setPadding(8, 8, 8, 8)
                    tableRow.addView(nomeTextView)

                    val codigoTextView = TextView(this@ClientesActivity)
                    codigoTextView.text = codigo
                    codigoTextView.setPadding(8, 8, 8, 8)
                    tableRow.addView(codigoTextView)

                    val orcamentoTextView = TextView(this@ClientesActivity)
                    orcamentoTextView.text = orcamento
                    orcamentoTextView.setPadding(8, 8, 8, 8)
                    tableRow.addView(orcamentoTextView)

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


