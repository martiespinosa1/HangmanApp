package com.example.hagmanapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.util.Random



@Composable
fun Game(navController: NavController, selectedDifficulty: String) {
    val abcdario = remember {
        listOf(
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "Ñ",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )
    }

    val palabras3Letras = remember {
        listOf(
            "sol",
            "pan",
            "luz",
            "mar",
            "uva",
            "rio",
            "oro",
            "ley",
            "voz",
            "sal"
        )
    }
    val palabras4Letras = remember {
        listOf(
            "casa",
            "dado",
            "nube",
            "pato",
            "vino",
            "moto",
            "azul",
            "rojo",
            "flor",
            "gris"
        )
    }
    val palabras5Letras = remember {
        listOf(
            "valor",
            "libro",
            "cielo",
            "reloj",
            "tigre",
            "pilar",
            "verde",
            "yegua",
            "nieve",
            "casco"
        )
    }

    var randomWord by remember { mutableStateOf("") }
    var palabraOculta by remember { mutableStateOf(generarPalabraOculta(selectedDifficulty)) }
    val random = Random()
    var attempts by remember { mutableIntStateOf(0) }
    var fallos by remember { mutableStateOf(0) }

    Image(
        painter = painterResource(id = R.drawable.gris),
        contentDescription = "fondo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val randomIndex: Int
        var randomWordInitialized by remember { mutableStateOf(false) }

        if (!randomWordInitialized) {
            when (selectedDifficulty) {
                "FÁCIL" -> {
                    randomIndex = random.nextInt(palabras3Letras.size)
                    randomWord = palabras3Letras[randomIndex]
                }
                "NORMAL" -> {
                    randomIndex = random.nextInt(palabras4Letras.size)
                    randomWord = palabras4Letras[randomIndex]
                }
                "DIFÍCIL" -> {
                    randomIndex = random.nextInt(palabras5Letras.size)
                    randomWord = palabras5Letras[randomIndex]
                }
            }
            randomWordInitialized = true
        }

        Text(
            text = palabraOculta,
            fontSize = 65.sp,
            fontFamily = customFontFamily1,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        val hangmanArray = arrayOf(R.drawable.hangman0, R.drawable.hangman1, R.drawable.hangman2, R.drawable.hangman3, R.drawable.hangman4, R.drawable.hangman5, R.drawable.hangman6, R.drawable.hangman7, R.drawable.hangman8, R.drawable.hangman9)

        Image(
            painter = painterResource(hangmanArray[fallos]),
            contentDescription = "hangman",
            modifier = Modifier.requiredSize(200.dp)
        )

        val buttonColor = ButtonDefaults.buttonColors(
            containerColor = Color(.15f, .15f, .15f), // Gris oscuro
            contentColor = Color(.85f, .85f, .85f) // Gris claro
        )

        // CREACION DE LOS BOTONES (TECLAS)
        var index = -1
        var isGameOver by remember { mutableStateOf(false) }
        var showCongratsMessage by remember { mutableStateOf(false) }

        for (i in 1..26) {
            if (i % 6 == 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (j in 1..6) {
                        index++
                        if (index < abcdario.size) {
                            val letra = abcdario[index]
                            var isButtonEnabled by remember { mutableStateOf(true) }

                            OutlinedButton(
                                onClick = {
                                    if (isButtonEnabled && !isGameOver) {
                                        attempts++
                                        val letraEnPalabra = letraEnPalabra(randomWord, letra)
                                        if (letraEnPalabra) {
                                            palabraOculta = ponerLetras(randomWord, palabraOculta, letra)
                                            if (!palabraOculta.contains("_")) {
                                                // El jugador ha adivinado la palabra
                                                isGameOver = true
                                                showCongratsMessage = true
                                            }
                                        } else {
                                            fallos++
                                            if (fallos == 9) {
                                                isGameOver = true
                                            }
                                        }

                                        // Deshabilitar el botón después de hacer clic
                                        isButtonEnabled = false
                                    }
                                },
                                modifier = Modifier.size(50.dp),
                                colors = buttonColor,
                                shape = MaterialTheme.shapes.medium.copy(CornerSize(10.dp)),
                                contentPadding = PaddingValues(0.dp),
                                enabled = isButtonEnabled
                            ) {
                                Text(text = letra, fontSize = 15.sp, fontFamily = customFontFamily1, color = Color.LightGray)
                            }
                            if (j < 6) Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }
            }
        }

        // Fin de partida
        if (isGameOver) {
            LaunchedEffect(palabraOculta) {
                delay(1000)

                // Determinar si el juego se ganó o se perdió
                val hasWon = !palabraOculta.contains("_") && fallos < 9

                // Navegar a ResultScreen con el resultado
                navController.navigate(
                    Routes.Result.createRoute(
                        hasWon = hasWon,
                        palabra = randomWord,
                        intentos = attempts,
                        fallos = fallos,
                        diff = selectedDifficulty
                    )
                )
            }
        }

        Text(
            text = "INTENTOS: $attempts",
            fontSize = 24.sp,
            fontFamily = customFontFamily1,
            modifier = Modifier.padding(top = 15.dp)
        )

    }
}


fun generarPalabraOculta(selectedDifficulty: String): String {

    return when (selectedDifficulty) {
        "FÁCIL" -> "_ _ _"
        "NORMAL" -> "_ _ _ _"
        "DIFÍCIL" -> "_ _ _ _ _"
        else -> ""
    }
}

fun letraEnPalabra(palabraRandom: String, letra: String): Boolean {
    println(palabraRandom)
    println(letra)
    println(palabraRandom.contains(letra.lowercase()))
    return palabraRandom.contains(letra.lowercase())
}

fun ponerLetras(palabra: String, palabraOculta: String, letra: String): String {
    val nuevaPalabraOculta = StringBuilder(palabraOculta)
    for (i in palabra.indices) {
        if (palabra[i].uppercaseChar() == letra[0]) {
            nuevaPalabraOculta.setCharAt(i * 2, letra[0])
        }
    }
    println("palabra: $palabra")
    println("letra: $letra")
    println("palabraOculta actualizada: $nuevaPalabraOculta")
    return nuevaPalabraOculta.toString()
}



