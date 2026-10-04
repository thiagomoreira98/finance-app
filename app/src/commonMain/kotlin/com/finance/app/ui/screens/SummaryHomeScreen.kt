package com.finance.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.finance.app.data.model.FinancialSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryHomeScreen(
    summary: FinancialSummary
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumo Financeiro") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Saldo Projetado do Mês", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "R$ ${summary.projectedBalance}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryMetricCard(
                    title = "Rendas",
                    value = "R$ ${summary.totalIncome}",
                    modifier = Modifier.weight(1f)
                )
                SummaryMetricCard(
                    title = "Custos Fixos",
                    value = "R$ ${summary.totalFixedCosts}",
                    modifier = Modifier.weight(1f)
                )
            }

            SummaryMetricCard(
                title = "Lançamentos (Mês)",
                value = "R$ ${summary.totalMonthlyCreditCard}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SummaryMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
