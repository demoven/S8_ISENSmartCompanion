package fr.isen.fernando.isensmartcompanion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.fernando.isensmartcompanion.database.AppDatabase
import fr.isen.fernando.isensmartcompanion.models.EventModel
import fr.isen.fernando.isensmartcompanion.screens.EventScreen
import fr.isen.fernando.isensmartcompanion.screens.HistoryScreen
import fr.isen.fernando.isensmartcompanion.screens.MessageCard
import fr.isen.fernando.isensmartcompanion.screens.TabView
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
            val db = AppDatabase.getInstance(this)
            MessageCard(getString(R.string.app_name), db)

            val homeTab = TabBarItem(
                title = getString(R.string.home),
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val eventsTab = TabBarItem(
                title = getString(R.string.events),
                selectedIcon = Icons.Filled.Notifications,
                unselectedIcon = Icons.Outlined.Notifications
            )
            val historyTab = TabBarItem(
                title = getString(R.string.history),
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.List
            )

            val tabBarItems = listOf(homeTab, eventsTab, historyTab)

            val navController = rememberNavController()

            ISENSmartCompanionTheme {
                Scaffold(

                    bottomBar = {
                        TabView(tabBarItems, navController)
                    }
                ) {
                    NavHost(navController = navController, startDestination = homeTab.title) {
                        composable(homeTab.title) { MessageCard(getString(R.string.app_name), db) }
                        composable(eventsTab.title) {
                            EventScreen(eventHandler = { event ->
                                startEventDataActivity(event)
                            })
                        }
                        composable(historyTab.title) { HistoryScreen(db) }
                    }
                }
            }
        }
    }

    fun startEventDataActivity(event: EventModel) {
        val intent = Intent(this, EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.eventExtraKey, event)
        }
        startActivity(intent)
    }

//    override fun onResume() {
//        super.onResume()
//        Log.d("lifecycle","Main Resmue")
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.d("lifecycle","Main Restart")
//    }
//
//    override fun onDestroy() {
//        Log.d("lifecycle","Main destroy")
//        super.onDestroy()
//
//    }
//
//    override fun onStop() {
//        Log.d("lifecycle","Main stop")
//        super.onStop()
//    }
}
