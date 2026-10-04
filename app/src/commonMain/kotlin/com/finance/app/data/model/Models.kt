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
data class TransactionItem(
    val id: String = "",
    val name: String,
    val date: String,
    val totalAmount: Double,
    val installments: Int,
    val card: String
)

@Serializable
data class FixedCostItem(
    val id: String = "",
    val name: String,
    val amount: Double
)

@Serializable
data class IncomeItem(
    val id: String = "",
    val source: String,
    val amount: Double
)

@Serializable
data class FinancialSummary(
    val totalIncome: Double,
    val totalFixedCosts: Double,
    val totalMonthlyCreditCard: Double,
    val projectedBalance: Double
)
