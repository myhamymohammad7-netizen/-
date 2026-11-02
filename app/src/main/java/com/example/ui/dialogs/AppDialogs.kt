package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Priority
import com.example.model.TaskCategory
import com.example.ui.theme.NoteColor1
import com.example.ui.theme.NoteColor2
import com.example.ui.theme.NoteColor3
import com.example.ui.theme.NoteColor4
import com.example.ui.theme.NoteColor5

@Composable
fun AddTaskDialog(
  isFa: Boolean,
  onDismiss: () -> Unit,
  onConfirm: (title: String, description: String, category: TaskCategory, priority: Priority, dueDate: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(TaskCategory.PERSONAL) }
  var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
  var dueDate by remember { mutableStateOf(if (isFa) "امروز" else "Today") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isFa) "تعریف کار جدید" else "Create New Task",
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text(if (isFa) "عنوان کار" else "Task Title") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_task_title_input"),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text(if (isFa) "توضیحات و جزییات (اختیاری)" else "Notes / Details (Optional)") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )

        // Category Selection
        Text(
          text = if (isFa) "دسته‌بندی:" else "Category:",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          TaskCategory.values().take(3).forEach { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(if (isFa) cat.titleFa else cat.titleEn, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White
              )
            )
          }
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          TaskCategory.values().drop(3).forEach { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(if (isFa) cat.titleFa else cat.titleEn, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White
              )
            )
          }
        }

        // Priority Selection
        Text(
          text = if (isFa) "اولویت:" else "Priority:",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Priority.values().forEach { prio ->
            FilterChip(
              selected = selectedPriority == prio,
              onClick = { selectedPriority = prio },
              label = { Text(if (isFa) prio.titleFa else prio.titleEn, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(prio.colorHex),
                selectedLabelColor = Color.White
              )
            )
          }
        }

        OutlinedTextField(
          value = dueDate,
          onValueChange = { dueDate = it },
          label = { Text(if (isFa) "سررسید / زمان (مثلاً: امروز، فردا)" else "Due Date (e.g. Today, Tomorrow)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, description, selectedCategory, selectedPriority, dueDate)
          }
        },
        enabled = title.isNotBlank(),
        modifier = Modifier.testTag("confirm_add_task_btn")
      ) {
        Text(if (isFa) "افزودن کار" else "Add Task")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text(if (isFa) "انصراف" else "Cancel")
      }
    }
  )
}

@Composable
fun AddHabitDialog(
  isFa: Boolean,
  onDismiss: () -> Unit,
  onConfirm: (title: String, description: String, category: String, targetDays: Int) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var category by remember { mutableStateOf(if (isFa) "سبک زندگی" else "Lifestyle") }
  var targetDays by remember { mutableIntStateOf(7) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isFa) "ساخت عادت جدید" else "New Daily Habit",
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text(if (isFa) "عنوان عادت (مثلاً: پیاده‌روی ۲۰ دقیقه)" else "Habit Title") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_habit_title_input"),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text(if (isFa) "انگیزه یا توضیح کوتاه" else "Description / Motivation") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text(if (isFa) "دسته‌بندی (مثلاً: ورزش، ذهن‌آگاهی)" else "Category") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Text(
          text = if (isFa) "هدف: $targetDays روز در هفته" else "Target: $targetDays days / week",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.primary
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(3, 5, 6, 7).forEach { days ->
            FilterChip(
              selected = targetDays == days,
              onClick = { targetDays = days },
              label = { Text(if (isFa) "$days روز" else "$days d") }
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, description, category, targetDays)
          }
        },
        enabled = title.isNotBlank(),
        modifier = Modifier.testTag("confirm_add_habit_btn")
      ) {
        Text(if (isFa) "افزودن عادت" else "Add Habit")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text(if (isFa) "انصراف" else "Cancel")
      }
    }
  )
}

@Composable
fun AddNoteDialog(
  isFa: Boolean,
  onDismiss: () -> Unit,
  onConfirm: (title: String, content: String, colorIndex: Int) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedColorIndex by remember { mutableIntStateOf(0) }

  val colors = listOf(NoteColor1, NoteColor2, NoteColor3, NoteColor4, NoteColor5)

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isFa) "نوشتن یادداشت جدید" else "New Note & Idea",
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text(if (isFa) "عنوان یادداشت" else "Note Title") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_note_title_input"),
          singleLine = true
        )

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text(if (isFa) "متن یادداشت..." else "Note content...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
          maxLines = 5
        )

        Text(
          text = if (isFa) "انتخاب رنگ تم یادداشت:" else "Pick Note Color:",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          colors.forEachIndexed { index, color ->
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { selectedColorIndex = index }
                .then(
                  if (selectedColorIndex == index) {
                    Modifier.border(2.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                  } else Modifier
                ),
              contentAlignment = Alignment.Center
            ) {
              if (selectedColorIndex == index) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color(0xFF1E293B),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() || content.isNotBlank()) {
            onConfirm(title.ifBlank { if (isFa) "بدون عنوان" else "Untitled" }, content, selectedColorIndex)
          }
        },
        enabled = title.isNotBlank() || content.isNotBlank(),
        modifier = Modifier.testTag("confirm_add_note_btn")
      ) {
        Text(if (isFa) "ثبت یادداشت" else "Save Note")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text(if (isFa) "انصراف" else "Cancel")
      }
    }
  )
}
