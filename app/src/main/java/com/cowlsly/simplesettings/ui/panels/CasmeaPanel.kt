package com.cowlsly.simplesettings.ui.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.data.UserPreferences

@Composable
fun CasmeaPanel(
    prefs: UserPreferences,
    onSave: (
        name: String,
        blood: String,
        allergies: String,
        medications: String,
        contact: String,
        conditions: String,
    ) -> Unit,
) {
    var name by remember(prefs.casmeaFullName) { mutableStateOf(prefs.casmeaFullName) }
    var blood by remember(prefs.casmeaBloodType) { mutableStateOf(prefs.casmeaBloodType) }
    var allergies by remember(prefs.casmeaAllergies) { mutableStateOf(prefs.casmeaAllergies) }
    var medications by remember(prefs.casmeaMedications) { mutableStateOf(prefs.casmeaMedications) }
    var contact by remember(prefs.casmeaEmergencyContact) { mutableStateOf(prefs.casmeaEmergencyContact) }
    var conditions by remember(prefs.casmeaConditions) { mutableStateOf(prefs.casmeaConditions) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("CASMEA medical info", style = MaterialTheme.typography.titleMedium)
        Text(
            "Single source of truth — CASMEA app reads this; edits only here.",
            style = MaterialTheme.typography.bodySmall,
        )
        OutlinedTextField(name, { name = it }, label = { Text("Full name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(blood, { blood = it }, label = { Text("Blood type") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(allergies, { allergies = it }, label = { Text("Allergies") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(medications, { medications = it }, label = { Text("Medications") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(conditions, { conditions = it }, label = { Text("Conditions") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(contact, { contact = it }, label = { Text("Emergency contact") }, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = { onSave(name, blood, allergies, medications, contact, conditions) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save medical info")
        }
    }
}