package fr.isen.fernando.isensmartcompanion.screens


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.fernando.isensmartcompanion.R
import fr.isen.fernando.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class MainScreen {


    @Composable
    fun MessageCard(title: String) {
        val context = LocalContext.current
        var userInput = remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.isen_logo),
                    contentDescription = context.getString(R.string.isen_logo),
                    modifier = Modifier.size(150.dp)
                )
                Text(text = title, textAlign = TextAlign.Center)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(8.dp)

            ) {
                TextField(
                    value = userInput.value,
                    onValueChange = { userInput.value = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "User input : ${userInput.value}",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Red, shape = CircleShape)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_forward),
                                contentDescription = "Envoyer"
                            )
                        }
                    }
                )
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun MessageCardPreview() {
        ISENSmartCompanionTheme {
            MessageCard(
                "Smart Companion"
            )
        }
    }

}