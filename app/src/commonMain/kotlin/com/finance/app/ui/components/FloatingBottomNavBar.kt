package com.finance.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finance.app.ui.navigation.Screen

@Composable
fun FloatingBottomNavBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Screen.HOME, Icons.Default.PieChart, currentScreen, onScreenSelected)
            NavItem(Screen.TRANSACTIONS, Icons.Default.Receipt, currentScreen, onScreenSelected)
            NavItem(Screen.CARDS, Icons.Default.Style, currentScreen, onScreenSelected)
            NavItem(Screen.FIXED_COSTS, Icons.Default.ReceiptLong, currentScreen, onScreenSelected)
            NavItem(Screen.INCOMES, Icons.Default.AttachMoney, currentScreen, onScreenSelected)
        }
    }
}

@Composable
private fun NavItem(
    screen: Screen,
    icon: ImageVector,
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val isSelected = screen == currentScreen
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onScreenSelected(screen) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = screen.title,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            if (isSelected) {
                Text(
                    text = screen.title,
                    fontSize = 10.sp,
                    color = contentColor
                )
            }
        }
    }
}
