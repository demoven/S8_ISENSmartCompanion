package fr.isen.fernando.isensmartcompanion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.fernando.isensmartcompanion.screens.BottomNavigation
import fr.isen.fernando.isensmartcompanion.screens.EventsScreen
import fr.isen.fernando.isensmartcompanion.screens.HistoryScreen
import fr.isen.fernando.isensmartcompanion.screens.MainScreen
import fr.isen.fernando.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
        @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                val mainScreen = MainScreen()
                mainScreen.MessageCard(getString(R.string.app_name))

                val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
                val eventsTab = TabBarItem(title = "Events", selectedIcon = Icons.Filled.Notifications, unselectedIcon = Icons.Outlined.Notifications, badgeAmount = 7)
                val historyTab = TabBarItem(title = "History", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings)

                val tabBarItems = listOf(homeTab, eventsTab, historyTab)

                val navController = rememberNavController()

                ISENSmartCompanionTheme {
                    Scaffold(

                        bottomBar = {
                            BottomNavigation().TabView(tabBarItems, navController)
                        }
                    ) {
                        NavHost(navController = navController, startDestination = homeTab.title) {
                            composable(homeTab.title) { MainScreen().MessageCard(getString(R.string.app_name)) }
                            composable(eventsTab.title) { EventsScreen().EventScreen() }
                            composable(historyTab.title) { HistoryScreen() }
                        }
                    }
                }
            }
        }

}
