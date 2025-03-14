package com.example.calcul_imc

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMCApp()
        }
    }
}

@Composable
fun IMCApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "formulaire") {
        composable("formulaire") {
            IMCForm(navController)
        }
        composable("resultat/{nom}/{prenom}/{age}/{sexe}/{poids}/{taille}/{activite}/{imc}/{classification}") { backStackEntry ->
            val nom = backStackEntry.arguments?.getString("nom") ?: ""
            val prenom = backStackEntry.arguments?.getString("prenom") ?: ""
            val age = backStackEntry.arguments?.getString("age") ?: ""
            val sexe = backStackEntry.arguments?.getString("sexe") ?: ""
            val poids = backStackEntry.arguments?.getString("poids")?.toInt() ?: 60
            val taille = backStackEntry.arguments?.getString("taille") ?: ""
            val activite = backStackEntry.arguments?.getString("activite") ?: ""
            val imc = backStackEntry.arguments?.getString("imc")?.toDoubleOrNull() ?: 0.0
            val classification = backStackEntry.arguments?.getString("classification") ?: ""

            IMCResult(nom, prenom, age, sexe, poids, taille, activite, imc, classification, navController)
        }
    }
}

@Composable
fun IMCForm(navController: NavController) {
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var dateNaissance by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var poids by remember { mutableStateOf(60) }
    var taille by remember { mutableStateOf("") }
    var sexe by remember { mutableStateOf("") }
    var activite by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenue sur l'application de calcul de l'IMC !",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.Blue),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Cette application vous permet de calculer votre IMC en fonction de votre poids et taille.\nEntrez vos informations ci-dessous.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(value = nom, onValueChange = { nom = it }, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = prenom, onValueChange = { prenom = it }, label = { Text("Prénom") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
        DatePickerButton(dateNaissance) { newDate ->
            dateNaissance = newDate
            age = calculerAge(newDate)
        }
        Text("Âge: $age ans", modifier = Modifier.padding(8.dp))

        DropdownSelector("Sexe", listOf("Homme", "Femme"), sexe) { sexe = it }
        DropdownSelector("Activité Physique", listOf("Sédentaire", "Faible", "Actif", "Sportif", "Athlète"), activite) { activite = it }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { if (poids > 30) poids-- }) { Text("-") }
            Text(" Poids: $poids kg ", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = { if (poids < 200) poids++ }) { Text("+") }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = taille, onValueChange = { taille = it }, label = { Text("Taille (m)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        CustomButton("Calculer") {
            val imc = calculerIMC(poids, taille)
            if (imc == "Erreur") return@CustomButton

            val classification = classerIMC(imc.toDouble())
            navController.navigate("resultat/$nom/$prenom/$age/$sexe/$poids/$taille/$activite/$imc/$classification")
        }
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

@Composable
fun IMCResult(nom: String, prenom: String, age: String, sexe: String, poids: Int, taille: String, activite: String, imc: Double, classification: String, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Résultat du Calcul IMC",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.Blue),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text("Nom : $nom")
        Text("Prénom : $prenom")
        Text("Âge : $age ans")
        Text("Sexe : $sexe")
        Text("Poids : $poids kg")
        Text("Taille : $taille m")
        Text("Activité : $activite")

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage de l'IMC
        Text(
            "IMC : $imc",
            style = MaterialTheme.typography.headlineSmall,
            color = if (imc < 18.5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Affichage de la classification de l'IMC
        Text(
            "Classification : $classification",
            style = MaterialTheme.typography.bodyLarge,
            color = when (classification) {
                "Sous poids" -> MaterialTheme.colorScheme.error
                "Poids normal" -> MaterialTheme.colorScheme.primary
                "Surpoids" -> MaterialTheme.colorScheme.secondary
                "Obésité modérée", "Obésité sévère", "Obésité morbide" -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onBackground
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton de retour pour refaire un calcul
        CustomButton("Faire un autre calcul") {
            navController.navigate("formulaire")
        }
    }
}

@Composable
fun DatePickerButton(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Button(
        onClick = {
            DatePickerDialog(context, { _, year, month, day ->
                onDateSelected("$day-${month + 1}-$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(if (selectedDate.isEmpty()) "Sélectionner une date" else "Date: $selectedDate")
    }
}

@Composable
fun DropdownSelector(label: String, options: List<String>, selected: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(if (selected.isEmpty()) label else selected)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onValueChange(option)
                    expanded = false
                })
            }
        }
    }
}

fun calculerIMC(poids: Int, taille: String): String {
    return try {
        val tailleM = taille.toDouble()
        val imc = poids / (tailleM * tailleM)
        "%.2f".format(imc)
    } catch (e: Exception) {
        "Erreur"
    }
}

fun classerIMC(imc: Double): String {
    return when {
        imc < 18.5 -> "Sous poids"
        imc < 25.0 -> "Poids normal"
        imc < 30.0 -> "Surpoids"
        imc < 35.0 -> "Obésité modérée"
        imc < 40.0 -> "Obésité sévère"
        else -> "Obésité morbide"
    }
}

fun calculerAge(dateNaissance: String): String {
    val parts = dateNaissance.split("-")
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val birthYear = parts[2].toInt()
    return (currentYear - birthYear).toString()
}
