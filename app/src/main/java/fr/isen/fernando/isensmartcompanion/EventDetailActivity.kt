package fr.isen.fernando.isensmartcompanion

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.Manifest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.fernando.isensmartcompanion.models.EventModel
import fr.isen.fernando.isensmartcompanion.ui.theme.ISENSmartCompanionTheme


class EventDetailActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getSerializableExtra(EventDetailActivity.eventExtraKey) as? EventModel
        enableEdgeToEdge()

        setContent {
            ISENSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    if (event != null) {
                        event.isNotified = getIsNotified(this, event.id)
                        val isNotified = remember { mutableStateOf(event.isNotified) }
                        EventDetail(event, this)

                    }
                }
            }
        }
    }

    companion object {
        val eventExtraKey = "eventExtraKey"
    }

}

@Composable
fun EventDetail(event: EventModel, context: Context) {
    val isNotified = remember { mutableStateOf(getIsNotified(context, event.id)) }


    Column(
        modifier = Modifier
            .padding(vertical = 48.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                // Close the activity et be back on the event list
                context.startActivity(Intent(context, MainActivity::class.java))
            })
            {
                Image(
                    painter = painterResource(R.drawable.xmark),
                    contentDescription = (context.getString(R.string.close)),
                    modifier = Modifier
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(Color.Red)
                )
            }

            IconButton(onClick = {
                isNotified.value = !isNotified.value
                event.isNotified = isNotified.value
                saveIsNotified(context, event.id, isNotified.value)
                if (isNotified.value) {
                    sendNotification(context, event)
                }
                Log.d("Notification", "Notification status: ${isNotified.value}")
            })
            {
                Image(
                    painter = painterResource(R.drawable.bell),
                    contentDescription = (context.getString(R.string.notification_icon)),
                    modifier = Modifier
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(if (isNotified.value) Color.Green else Color.Red)
                )
            }
        }
        Text(
            event.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp,
        )
        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = (context.getString(R.string.calendar_icon)),
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 4.dp)
            )
            Text(event.date, modifier = Modifier.padding(start = 8.dp))
        }
        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.location),
                contentDescription = (context.getString(R.string.location_icon)),
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 4.dp)
            )
            Text(event.location, modifier = Modifier.padding(start = 8.dp))
        }

        Text(
            event.description,
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 20.sp
        )

    }
}

fun sendNotification(context: Context, event: EventModel) {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
        val intent = Intent(context, EventDetailActivity::class.java).apply {
            putExtra("event", event)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "event_channel")
            .setSmallIcon(R.drawable.calendar)
            .setContentTitle("Event Reminder")
            .setContentText("Don't forget the event: ${event.title}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_channel",
                "Event Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for event notifications"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(0, builder.build())
        }
    }, 10000) // 10 seconds delay
}

fun saveIsNotified(context: Context, eventId: String, isNotified: Boolean) {
    val sharedPreferences = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean(eventId, isNotified)
        apply()
    }
}

fun getIsNotified(context: Context, eventId: String): Boolean {
    val sharedPreferences = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(eventId, false)
}
