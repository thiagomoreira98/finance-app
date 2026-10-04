package com.finance.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finance.app.data.model.CardType
import com.finance.app.data.model.CreditCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    cards: List<CreditCard>,
    onAddCard: (CreditCard) -> Unit,
    onDeleteCard: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var lastFourDigits by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(CardType.PHYSICAL) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Cartões") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Cartão")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (cards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum cartão cadastrado.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cards) { card ->
                        CardItemRow(card = card, onDelete = { onDeleteCard(card.id) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddDialog = false
                errorMessage = ""
            },
            title = { Text("Novo Cartão de Crédito") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do Cartão (ex: Nubank)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = lastFourDigits,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) lastFourDigits = it },
                        label = { Text("Últimos 4 Dígitos") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Tipo do Cartão:", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = selectedType == CardType.PHYSICAL,
                            onClick = { selectedType = CardType.PHYSICAL },
                            label = { Text("Físico") }
                        )
                        FilterChip(
                            selected = selectedType == CardType.DIGITAL,
                            onClick = { selectedType = CardType.DIGITAL },
                            label = { Text("Digital") }
                        )
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
                        if (name.isBlank()) {
                            errorMessage = "Informe o nome do cartão"
                        } else if (lastFourDigits.length != 4) {
                            errorMessage = "Informe exatamente os 4 últimos dígitos"
                        } else {
                            onAddCard(
                                CreditCard(
                                    name = name,
                                    lastFourDigits = lastFourDigits,
                                    type = selectedType
                                )
                            )
                            name = ""
                            lastFourDigits = ""
                            selectedType = CardType.PHYSICAL
                            errorMessage = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showAddDialog = false
                    errorMessage = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun CardItemRow(
    card: CreditCard,
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
                Icon(Icons.Default.CreditCard, contentDescription = null)
                Column {
                    Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "•••• ${card.lastFourDigits} (${if (card.type == CardType.PHYSICAL) "Físico" else "Digital"})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Remover", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
