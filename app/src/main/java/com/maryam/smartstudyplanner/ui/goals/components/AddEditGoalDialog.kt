package com.maryam.smartstudyplanner.ui.goals.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import com.maryam.smartstudyplanner.ui.tasks.components.DropdownField
import com.maryam.smartstudyplanner.util.endOfToday
import com.maryam.smartstudyplanner.util.startOfToday

@Composable
fun AddEditGoalDialog(
    goal: StudyGoalEntity?,
    subjects: List<SubjectEntity>,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        targetMinutes: Long,
        subjectId: Long?,
        startDate: Long,
        endDate: Long
    ) -> Unit
) {
    var title by remember { mutableStateOf(goal?.title ?: "") }
    var targetMinutesText by remember { mutableStateOf(goal?.targetMinutes?.toString() ?: "") }
    var selectedSubjectId by remember { mutableStateOf(goal?.subjectId) }
    var startDate by remember { mutableStateOf(goal?.startDate ?: startOfToday()) }
    var endDate by remember { mutableStateOf(goal?.endDate ?: endOfToday()) }

    var titleError by remember { mutableStateOf(false) }
    var minutesError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (goal == null) "Add Goal" else "Edit Goal") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; titleError = false },
                    label = { Text("Goal title (e.g. Daily Study Goal)") },
                    isError = titleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = targetMinutesText,
                    onValueChange = { targetMinutesText = it; minutesError = false },
                    label = { Text("Target minutes") },
                    isError = minutesError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownField(
                    label = "Subject (optional — leave blank for overall goal)",
                    items = subjects,
                    selectedItem = subjects.find { it.subjectId == selectedSubjectId },
                    itemLabel = { it.name },
                    onItemSelected = { selectedSubjectId = it.subjectId }
                )
                Spacer(modifier = Modifier.height(8.dp))
                GoalDateField(
                    label = "Start date",
                    selectedDate = startDate,
                    onDateSelected = { startDate = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                GoalDateField(
                    label = "End date",
                    selectedDate = endDate,
                    onDateSelected = { endDate = it }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val minutes = targetMinutesText.toLongOrNull()
                when {
                    title.isBlank() -> titleError = true
                    minutes == null || minutes <= 0 -> minutesError = true
                    else -> onConfirm(title.trim(), minutes, selectedSubjectId, startDate, endDate)
                }
            }) {
                Text(if (goal == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}