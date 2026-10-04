package com.finance.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.finance.app.data.model.CreditCard
import com.finance.app.ui.components.FloatingBottomNavBar
import com.finance.app.ui.navigation.Screen
import com.finance.app.ui.screens.CardsScreen
import com.finance.app.ui.screens.TransactionsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var cards by remember { mutableStateOf(listOf<CreditCard>()) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                FloatingBottomNavBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (currentScreen) {
                    Screen.HOME -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Home Screen")
                        }
                    }
                    Screen.TRANSACTIONS -> {
                        TransactionsScreen(
                            transactions = listOf(),
                            availableCards = cards,
                            onAddTransaction = {},
                            onUpdateTransaction = {},
                            onDeleteTransaction = {}
                        )
                    }
                    Screen.CARDS -> {
                        CardsScreen(
                            cards = cards,
                            onAddCard = { newCard ->
                                cards = cards + newCard.copy(id = (cards.size + 1).toString())
                            },
                            onDeleteCard = { cardId ->
                                cards = cards.filter { it.id != cardId }
                            }
                        )
                    }
                    Screen.FIXED_COSTS -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Custos Fixos Screen")
                        }
                    }
                    Screen.INCOMES -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Rendas Screen")
                        }
                    }
                }
            }
        }
    }
}
