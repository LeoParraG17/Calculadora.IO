package com.leoparra.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Pantalla("0")
        BotonesCalculadora()
    }
}

@Composable
fun Pantalla(texto: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = texto,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun BotonesCalculadora() {
    val botones = listOf(
        listOf("C", "←", "%", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=", "√")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        botones.forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fila.forEach { boton ->
                    BotonCalculadora(boton, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun BotonCalculadora(valor: String, modifier: Modifier = Modifier) {
    val isOperator = valor in listOf("+", "-", "*", "/", "=", "%", "√")
    val isSpecial = valor in listOf("C", "←", ".")
    val buttonColor by animateColorAsState(
        when {
            isOperator -> Color(0xFFBB86FC) // Morado claro para operadores
            isSpecial -> Color(0xFF03DAC6) // Verde claro para botones especiales
            else -> Color(0xFF6200EE) // Morado oscuro para números
        }
    )
    val borderColor by animateColorAsState(
        when {
            isOperator -> Color(0xFF3700B3) // Morado oscuro para bordes de operadores
            isSpecial -> Color(0xFF018786) // Verde oscuro para bordes de botones especiales
            else -> Color(0xFFBB86FC) // Morado claro para bordes de números
        }
    )

    Button(
        onClick = { /* No hacer nada */ },
        modifier = modifier
            .aspectRatio(1f)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .semantics { contentDescription = "Botón $valor" },
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
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
        BotonesCalculadora()
    }
}