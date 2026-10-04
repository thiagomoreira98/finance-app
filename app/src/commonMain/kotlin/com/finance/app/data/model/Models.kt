package com.finance.app.data.model

import kotlinx.serialization.Serializable

enum class CardType {
    PHYSICAL,
    DIGITAL
}

@Serializable
data class CreditCard(
    val id: String = "",
    val name: String,
    val lastFourDigits: String,
    val type: CardType
)

@Serializable
data class CreditCardItem(
    val id: String = "",
    val nome: String,
    val data: String,
    val valorTotal: Double,
    val parcelas: Int,
    val cartao: String
)

@Serializable
data class FixedCostItem(
    val id: String = "",
    val nome: String,
    val valor: Double,
    val diaVencimento: Int,
    val categoria: String
)

@Serializable
data class IncomeItem(
    val id: String = "",
    val origem: String,
    val valor: Double,
    val diaRecebimento: Int
)

@Serializable
data class FinancialSummary(
    val totalRendas: Double,
    val totalCustosFixos: Double,
    val totalCartaoCreditoMes: Double,
    val saldoProjetado: Double
)
