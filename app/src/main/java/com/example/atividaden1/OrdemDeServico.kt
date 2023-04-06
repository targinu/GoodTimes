package com.example.atividaden1

data class OrdemDeServico(val cliente: Cliente,val nomeServico: String? = null,val id: String? = null,
                          val preco: Float? = null, val desconto: Float? = null,
                          val total: Float? = null, val comentario: String? = null) {
}