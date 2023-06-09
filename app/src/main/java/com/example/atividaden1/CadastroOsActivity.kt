package com.example.atividaden1

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.atividaden1.databinding.ActivityCadastrarOsBinding
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastroOsActivity: AppCompatActivity(){

    private lateinit var binding: ActivityCadastrarOsBinding
    private lateinit var database: DatabaseReference
    private lateinit var searchView: SearchView

    //TESTANDO A FUNÇÃO DE SELECIONAR CLIENTE
    private var selectedCliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarOsBinding.inflate(layoutInflater)
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

        //Adiciona um listener para adicionar os clientes na tabela
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpa as linhas da tabela
                tableLayout.removeAllViews()

                //Preenche a tabela com os dados dos clientes
                for (clienteSnapshot in snapshot.children) {
                    val nome = clienteSnapshot.child("nome").getValue(String::class.java)
                    val codigo = clienteSnapshot.child("codigo").getValue(String::class.java)
                    val orcamento = clienteSnapshot.child("orcamento").getValue(Float::class.java)

                    //TESTANDO FUNÇÃO DE DESTACAR CLIENTE SELECIONADO
                    val clienteId = clienteSnapshot.key
                    //TERMINA AQUI

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

                    //Função para selecionar e destacar cliente
                    //Declara uma variável para armazenar o ID do cliente selecionado
                    var clienteSelecionadoId: String? = null

                    //Verifica se o ID da linha corresponde ao ID do cliente selecionado
                    if (clienteId == clienteSelecionadoId) {
                        tableRow.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                    } else {
                        tableRow.setBackgroundColor(resources.getColor(android.R.color.white))
                    }

                    //Adiciona um listener para quando a linha for clicada
                    tableRow.setOnClickListener {

                        selectedCliente = Cliente(nome, codigo, orcamento ?: 0f)
                        Toast.makeText(this@CadastroOsActivity, "Cliente selecionado:" +
                                " $selectedCliente", Toast.LENGTH_SHORT).show()

                        //Armazena o ID do cliente selecionado
                        clienteSelecionadoId = clienteId

                        //Destaca a linha do cliente selecionado e remove o destaque das outras linhas
                        for (i in 0 until tableLayout.childCount) {
                            val row = tableLayout.getChildAt(i) as TableRow
                            val rowClienteId = snapshot.children.elementAt(i).key
                            if (rowClienteId == clienteSelecionadoId) {
                                row.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                            } else {
                                row.setBackgroundColor(resources.getColor(android.R.color.white))
                            }
                        }
                    }

                    //Adiciona os dados na tabela
                    tableLayout.addView(tableRow)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "Falha ao carregar clientes.", error.toException())
            }
        })

        //Função que calcula o valor total
        //Captura as referências das Views
        val editTextPreco = binding.editTextPreco
        val editTextDesconto = binding.editTextDesconto
        val textViewValorTotal = binding.textViewValorTotal

        //Adiciona um listener para atualizar o valor total quando o preço ou o desconto forem alterados
        editTextPreco.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calcularValorTotal(editTextPreco, editTextDesconto, textViewValorTotal)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Não é necessário fazer nada aqui, porém é necessario declarar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Não é necessário fazer nada aqui, porém é necessario declarar
            }
        })

        editTextDesconto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calcularValorTotal(editTextPreco, editTextDesconto, textViewValorTotal)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Não é necessário fazer nada aqui, porém é necessario declarar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Não é necessário fazer nada aqui, porém é necessario declarar
            }
        })

        //Salvar Ordem de Serviço no banco
        binding.buttonSalvar.setOnClickListener {
            val id = binding.editTextId.text.toString()

            //Verifica se o ID já está em uso por uma ordem aberta ou cancelada
            val ordensRef = FirebaseDatabase.getInstance().getReference("ordens")
            ordensRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ordensExistem = snapshot.exists()

                    if (ordensExistem) {
                        Toast.makeText(
                            this@CadastroOsActivity,
                            "Já existe uma ordem com o mesmo ID.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //Continua com o processo de salvar a ordem
                        val nomeServico = binding.editTextNomeServico.text.toString()
                        val preco = binding.editTextPreco.text.toString().toFloat()
                        val desconto = binding.editTextDesconto.text.toString().toFloat()
                        val total = binding.textViewValorTotal.text.toString().toFloat()

                        //Verifica se há um cliente selecionado
                        if (selectedCliente != null) {
                            database = FirebaseDatabase.getInstance().getReference("ordens")
                            val ordemDeServico =
                                OrdemDeServico(selectedCliente!!, nomeServico, id, preco, desconto, total)

                            //Salva a ordem de serviço com status "encerrada = false" e "cancelada = false"
                            ordemDeServico.encerrada = false
                            ordemDeServico.cancelada = false

                            database.child(id).setValue(ordemDeServico).addOnSuccessListener {

                                //Atualiza o orçamento do cliente selecionado
                                selectedCliente?.let { cliente ->
                                    val orcamentoAtualizado =
                                        cliente.orcamento?.minus(total)
                                    cliente.orcamento = orcamentoAtualizado
                                    val clientesRef =
                                        FirebaseDatabase.getInstance().getReference("clientes")
                                    clientesRef.child(cliente.codigo.toString())
                                        .setValue(cliente).addOnSuccessListener {
                                            Toast.makeText(this@CadastroOsActivity,"Ordem cadastrada com sucesso!",Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(this@CadastroOsActivity, "Erro ao atualizar o orçamento do cliente.", Toast.LENGTH_SHORT).show()
                                        }
                                }

                                //Limpa os campos de entrada de dados
                                binding.editTextNomeServico.text.clear()
                                binding.editTextId.text.clear()
                                binding.editTextPreco.text.clear()
                                binding.editTextDesconto.text.clear()
                                binding.textViewValorTotal.text = "0.00"
                            }.addOnFailureListener {
                                Toast.makeText(this@CadastroOsActivity, "Erro ao cadastrar a ordem de serviço.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                this@CadastroOsActivity,
                                "Selecione um cliente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@CadastroOsActivity,
                        "Falha ao verificar a existência de ordens.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        //Voltar para ordem de serviço
        binding.btnVoltar.setOnClickListener{
            val intent = Intent(this,OsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Método para calcular e exibir o valor total
    private fun calcularValorTotal(editTextPreco: EditText, editTextDesconto: EditText, textViewValorTotal: TextView) {
        val preco = editTextPreco.text.toString().toFloatOrNull() ?: 0f
        val desconto = editTextDesconto.text.toString().toFloatOrNull() ?: 0f
        val valorTotal = preco - desconto
        textViewValorTotal.text = valorTotal.toString()
    }
}
