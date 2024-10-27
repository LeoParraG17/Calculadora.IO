package com.leoparra.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoparra.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                CalculadoraApp()
            }
        }
    }
}

@Composable
fun CalculadoraApp() {
    var textoPantalla by remember { mutableStateOf("0") }
    var resultado by remember { mutableStateOf(0.0) }
    var operacion by remember { mutableStateOf("") }
    var entrada by remember { mutableStateOf("") }
    var nuevaOperacion by remember { mutableStateOf(true) }
    var historial by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA), Color(0xFF4FC3F7))
                )
            )
            .padding(16.dp)
    ) {
        Pantalla(textoPantalla)
        Spacer(modifier = Modifier.height(16.dp))
        Historial(historial)
        Spacer(modifier = Modifier.height(16.dp))
        BotonesCalculadora(
            onButtonClick = { valor ->
                when (valor) {
                    "C" -> {
                        textoPantalla = "0"
                        resultado = 0.0
                        operacion = ""
                        entrada = ""
                        nuevaOperacion = true
                    }
                    "←" -> {
                        if (entrada.isNotEmpty()) {
                            entrada = entrada.dropLast(1)
                            textoPantalla = entrada.ifEmpty { "0" }
                        }
                    }
                    "+", "-", "*", "/", "%" -> {
                        if (entrada.isNotEmpty()) {
                            resultado = entrada.toDoubleOrNull() ?: 0.0
                        }
                        operacion = valor
                        entrada = ""
                        nuevaOperacion = false
                    }
                    "=" -> {
                        val entradaDouble = entrada.toDoubleOrNull() ?: 0.0
                        resultado = when (operacion) {
                            "+" -> resultado + entradaDouble
                            "-" -> resultado - entradaDouble
                            "*" -> resultado * entradaDouble
                            "/" -> if (entradaDouble != 0.0) resultado / entradaDouble else Double.NaN
                            "%" -> resultado % entradaDouble
                            else -> resultado
                        }
                        textoPantalla = if (resultado % 1 == 0.0) resultado.toInt().toString() else resultado.toString()
                        historial = (historial + "$resultado $operacion $entradaDouble = $textoPantalla").takeLast(3)
                        entrada = ""
                        nuevaOperacion = true
                    }
                    else -> {
                        if (nuevaOperacion) {
                            entrada = valor
                            nuevaOperacion = false
                        } else {
                            entrada += valor
                        }
                        textoPantalla = entrada
                    }
                }
            }
        )
    }
}

@Composable
fun Pantalla(texto: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0288D1))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = texto,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun Historial(historial: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB3E5FC))
            .padding(8.dp)
    ) {
        historial.forEach { item ->
            Text(
                text = item,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BotonesCalculadora(onButtonClick: (String) -> Unit) {
    val botones = listOf(
        listOf("C", "←", "%", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=", "√")
    )

    Column {
        botones.forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fila.forEach { boton ->
                    BotonCalculadora(boton, onButtonClick)
                }
            }
        }
    }
}

@Composable
fun BotonCalculadora(valor: String, onButtonClick: (String) -> Unit) {
    val isOperator = valor in listOf("+", "-", "*", "/", "=", "%", "√")
    val isSpecial = valor in listOf("C", "←", ".")
    val buttonColor by animateColorAsState(
        when {
            isOperator -> Color(0xFF0288D1) // Azul oscuro para operadores
            isSpecial -> Color(0xFFFFA726) // Naranja para botones especiales
            else -> Color(0xFF4FC3F7) // Azul claro para números
        }
    )

    Button(
        onClick = { onButtonClick(valor) },
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp)
            .semantics { contentDescription = "Botón $valor" },
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(40.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(
            text = valor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculadoraApp() {
    CalculadoraTheme {
        CalculadoraApp()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantalla() {
    CalculadoraTheme {
        Pantalla("123")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBotonesCalculadora() {
    CalculadoraTheme {
        BotonesCalculadora(onButtonClick = {})
    }
}