package fr.isen.fernando.isensmartcompanion

import android.annotation.SuppressLint
import android.media.metrics.Event
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    if (event != null){
                        EventDetail(event)
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun EventDetail(event:EventModel){
    Column (modifier = Modifier.padding(48.dp)){
        Text(
            event.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp,
            )
        Row (
            modifier = Modifier.padding(top = 16.dp)
        ){
            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = ("calendar"),
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 4.dp)
            )
            Text(event.date, modifier = Modifier.padding(start = 8.dp))
        }
        Row (
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.location),
                contentDescription = ("location"),
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ISENSmartCompanionTheme {
        Greeting("Android")
    }
}