package com.finance.app.data.remote

import com.finance.app.data.model.AllDataResponse
import com.finance.app.data.model.CreditCard
import com.finance.app.data.model.FixedCostItem
import com.finance.app.data.model.IncomeItem
import com.finance.app.data.model.TransactionItem
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GasRequest<T>(
    val action: String,
    val payload: T
)

@Serializable
data class GasDeletePayload(
    val id: String
)

class FinanceApiClient(
    private val baseUrlProvider: () -> String = { AppConfig.webAppUrl }
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    suspend fun getAllData(): AllDataResponse {
        val url = "${baseUrlProvider()}?action=getAllData"
        val responseText = client.get(url).bodyAsText()
        return json.decodeFromString(responseText)
    }

    // Credit Cards
    suspend fun addCreditCard(card: CreditCard): String = postAction("addCreditCard", card)
    suspend fun updateCreditCard(card: CreditCard): String = postAction("updateCreditCard", card)
    suspend fun deleteCreditCard(id: String): String = postAction("deleteCreditCard", GasDeletePayload(id))

    // Transactions / Lancamentos
    suspend fun addTransaction(transaction: TransactionItem): String = postAction("addLancamento", transaction)
    suspend fun updateTransaction(transaction: TransactionItem): String = postAction("updateLancamento", transaction)
    suspend fun deleteTransaction(id: String): String = postAction("deleteLancamento", GasDeletePayload(id))

    // Fixed Costs
    suspend fun addFixedCost(fixedCost: FixedCostItem): String = postAction("addFixedCostItem", fixedCost)
    suspend fun updateFixedCost(fixedCost: FixedCostItem): String = postAction("updateFixedCostItem", fixedCost)
    suspend fun deleteFixedCost(id: String): String = postAction("deleteFixedCostItem", GasDeletePayload(id))

    // Incomes
    suspend fun addIncome(income: IncomeItem): String = postAction("addIncomeItem", income)
    suspend fun updateIncome(income: IncomeItem): String = postAction("updateIncomeItem", income)
    suspend fun deleteIncome(id: String): String = postAction("deleteIncomeItem", GasDeletePayload(id))

    private suspend inline fun <reified T> postAction(action: String, payload: T): String {
        val requestObj = GasRequest(action = action, payload = payload)
        val response = client.post(baseUrlProvider()) {
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        return response.bodyAsText()
    }
}
