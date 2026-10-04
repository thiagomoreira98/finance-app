package com.finance.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finance.app.data.model.CreditCard
import com.finance.app.data.model.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    transactions: List<TransactionItem>,
    availableCards: List<CreditCard>,
    onAddTransaction: (TransactionItem) -> Unit,
    onUpdateTransaction: (TransactionItem) -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<TransactionItem?>(null) }

    var nome by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var valorTotal by remember { mutableStateOf("") }
    var parcelas by remember { mutableStateOf("1") }
    var cartaoUsado by remember { mutableStateOf("") }
    var expandedCardDropdown by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val openAddDialog = {
        itemToEdit = null
        nome = ""
        data = ""
        valorTotal = ""
        parcelas = "1"
        cartaoUsado = availableCards.firstOrNull()?.name ?: ""
        errorMessage = ""
        showDialog = true
    }

    val openEditDialog = { item: TransactionItem ->
        itemToEdit = item
        nome = item.name
        data = item.date
        valorTotal = item.totalAmount.toString()
        parcelas = item.installments.toString()
        cartaoUsado = item.card
        errorMessage = ""
        showDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lançamentos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = openAddDialog) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Lançamento")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum lançamento registrado.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(transactions) { item ->
                        TransactionRow(
                            item = item,
                            onEdit = { openEditDialog(item) },
                            onDelete = { onDeleteTransaction(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
                errorMessage = ""
            },
            title = { Text(if (itemToEdit == null) "Novo Lançamento" else "Editar Lançamento") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome da Compra (ex: Mercado)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = data,
                        onValueChange = { data = it },
                        label = { Text("Data (ex: 04/10/2026)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = valorTotal,
                        onValueChange = { valorTotal = it },
                        label = { Text("Valor Total (R$)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = parcelas,
                        onValueChange = { if (it.all { char -> char.isDigit() }) parcelas = it },
                        label = { Text("Quantidade de Parcelas") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedCardDropdown,
                        onExpandedChange = { expandedCardDropdown = !expandedCardDropdown }
                    ) {
                        OutlinedTextField(
                            value = cartaoUsado,
                            onValueChange = { cartaoUsado = it },
                            label = { Text("Cartão Usado") },
                            readOnly = availableCards.isNotEmpty(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCardDropdown) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        if (availableCards.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expandedCardDropdown,
                                onDismissRequest = { expandedCardDropdown = false }
                            ) {
                                availableCards.forEach { card ->
                                    DropdownMenuItem(
                                        text = { Text("${card.name} (•••• ${card.lastFourDigits})") },
                                        onClick = {
                                            cartaoUsado = card.name
                                            expandedCardDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val valorParsed = valorTotal.toDoubleOrNull()
                        val parcelasParsed = parcelas.toIntOrNull()

                        if (nome.isBlank()) {
                            errorMessage = "Informe o nome da compra"
                        } else if (data.isBlank()) {
                            errorMessage = "Informe a data"
                        } else if (valorParsed == null || valorParsed <= 0) {
                            errorMessage = "Informe um valor total válido"
                        } else if (parcelasParsed == null || parcelasParsed <= 0) {
                            errorMessage = "Informe uma quantidade de parcelas válida"
                        } else if (cartaoUsado.isBlank()) {
                            errorMessage = "Informe o cartão usado"
                        } else {
                            val newItem = TransactionItem(
                                id = itemToEdit?.id ?: "",
                                name = nome,
                                date = data,
                                totalAmount = valorParsed,
                                installments = parcelasParsed,
                                card = cartaoUsado
                            )
                            if (itemToEdit == null) {
                                onAddTransaction(newItem)
                            } else {
                                onUpdateTransaction(newItem)
                            }
                            showDialog = false
                            errorMessage = ""
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDialog = false
                    errorMessage = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TransactionRow(
    item: TransactionItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Default.Receipt, contentDescription = null)
                Column {
                    Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "R$ ${item.totalAmount} (${item.installments}x) • Cartão: ${item.card}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Data: ${item.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
