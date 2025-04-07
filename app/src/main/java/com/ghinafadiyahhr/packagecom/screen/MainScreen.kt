package com.ghinafadiyahhr.packagecom.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ghinafadiyahhr.packagecom.R
import com.ghinafadiyahhr.packagecom.navigation.Screen
import com.ghinafadiyahhr.packagecom.ui.theme.Packagecomghinafadiyahhr607062300001asessment1Theme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var translated by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (translated) "Currency Converter" else "Konverter Mata Uang")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = if (translated) "About App" else "Tentang Aplikasi",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding), translated) { translated = it }
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier, translated: Boolean, onTranslateChange: (Boolean) -> Unit) {
    var idrAmount by rememberSaveable { mutableStateOf("") }
    var thbAmount by rememberSaveable { mutableStateOf("") }

    var idrError by rememberSaveable { mutableStateOf(false) }
    var thbError by rememberSaveable { mutableStateOf(false) }
    var showEmptyWarning by rememberSaveable { mutableStateOf(false) }

    var resultIDRtoUSD by rememberSaveable { mutableStateOf(0f) }
    var resultTHBtoUSD by rememberSaveable { mutableStateOf(0f) }

    val context = LocalContext.current

    val conversionRates = mapOf(
        "IDR" to 0.000063f,
        "THB" to 0.028f
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (translated) "Currency Converter" else "Konversi Mata Uang",
            style = MaterialTheme.typography.titleLarge
        )
        Image(
            painter = painterResource(id = R.drawable.uang),
            contentDescription = if (translated) "Currency Image" else "Gambar Uang",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        )


        OutlinedTextField(
            value = idrAmount,
            onValueChange = {
                idrAmount = it
                idrError = false
                showEmptyWarning = false
            },
            label = { Text(if (translated) "Enter IDR amount" else "Masukkan jumlah IDR") },
            isError = idrError,
            trailingIcon = { IconPicker(idrError, "IDR") },
            supportingText = {
                if (idrError) {
                    Text(
                        text = if (translated) "Invalid input!" else "Input tidak valid!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = thbAmount,
            onValueChange = {
                thbAmount = it
                thbError = false
                showEmptyWarning = false
            },
            label = { Text(if (translated) "Enter THB amount" else "Masukkan jumlah THB") },
            isError = thbError,
            trailingIcon = { IconPicker(thbError, "THB") },
            supportingText = {
                if (thbError) {
                    Text(
                        text = if (translated) "Invalid input!" else "Input tidak valid!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                idrError = false
                thbError = false

                val isIdrFilled = idrAmount.isNotBlank()
                val isThbFilled = thbAmount.isNotBlank()

                val idr = idrAmount.toFloatOrNull()
                val thb = thbAmount.toFloatOrNull()

                if (!isIdrFilled && !isThbFilled) {
                    showEmptyWarning = true
                    return@Button
                } else {
                    showEmptyWarning = false
                }

                if (isIdrFilled && idr == null) idrError = true
                if (isThbFilled && thb == null) thbError = true

                if (idrError || thbError) return@Button

                resultIDRtoUSD = if (idr != null && idr > 0) {
                    ((idr * conversionRates["IDR"]!!) * 100).roundToInt() / 100f
                } else 0f

                resultTHBtoUSD = if (thb != null && thb > 0) {
                    ((thb * conversionRates["THB"]!!) * 100).roundToInt() / 100f
                } else 0f
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (translated) "Convert to USD" else "Konversi ke USD")
        }

        // ✅ Warning tampil di bawah tombol kalau dua-duanya kosong
        if (showEmptyWarning) {
            Text(
                text = if (translated) "Please fill at least one field!" else "Harap isi minimal satu field!",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (resultIDRtoUSD > 0f || resultTHBtoUSD > 0f) {
            if (resultIDRtoUSD > 0f) {
                Text(
                    text = if (translated)
                        "Converted from IDR: $resultIDRtoUSD USD"
                    else
                        "Hasil dari IDR ke USD: $resultIDRtoUSD USD",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (resultTHBtoUSD > 0f) {
                Text(
                    text = if (translated)
                        "Converted from THB: $resultTHBtoUSD USD"
                    else
                        "Hasil dari THB ke USD: $resultTHBtoUSD USD",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Button(
                onClick = {
                    val shareText = buildString {
                        if (resultIDRtoUSD > 0f) {
                            append(
                                if (translated)
                                    "Converted from IDR: $resultIDRtoUSD USD\n"
                                else
                                    "Hasil dari IDR ke USD: $resultIDRtoUSD USD\n"
                            )
                        }
                        if (resultTHBtoUSD > 0f) {
                            append(
                                if (translated)
                                    "Converted from THB: $resultTHBtoUSD USD"
                                else
                                    "Hasil dari THB ke USD: $resultTHBtoUSD USD"
                            )
                        }
                    }
                    shareData(context, shareText)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = if (translated) "Share" else "Bagikan")
            }
        }

        Button(
            onClick = { onTranslateChange(!translated) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = if (translated) "Indonesian" else "English")
        }
    }
}


@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    Packagecomghinafadiyahhr607062300001asessment1Theme {
        MainScreen(rememberNavController())
    }
}
