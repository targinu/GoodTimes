package com.example.atividaden1

data class Cliente(val nome: String? = null, val codigo: String? = null, var orcamento: Float? = null){

    // Método para atualizar o orçamento do cliente subtraindo o valor total da ordem de serviço
    fun atualizarOrcamento(total: Float) {
        val novoOrcamento = orcamento?.minus(total)
        if (novoOrcamento != null) {
            if (novoOrcamento >= 0) {
                orcamento = novoOrcamento
            } else {
                throw IllegalArgumentException("O valor da ordem de serviço é maior do que o orçamento atual do cliente.")
            }
        }
    }

}