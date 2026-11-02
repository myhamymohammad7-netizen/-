package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.HabitItem
import com.example.ui.KarinoUiState

@Composable
fun HabitsScreen(
  state: KarinoUiState,
  onToggleHabitToday: (String) -> Unit,
  onDeleteHabit: (String) -> Unit,
  onAddHabitClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isFa = state.language == AppLanguage.PERSIAN

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        HabitSummaryHeader(
          completedCount = state.completedHabitsToday,
          totalCount = state.totalHabitsCount,
          isFa = isFa
        )
      }

      items(
        items = state.habits,
        key = { it.id }
      ) { habit ->
        HabitItemCard(
          habit = habit,
          isFa = isFa,
          onToggleToday = { onToggleHabitToday(habit.id) },
          onDelete = { onDeleteHabit(habit.id) }
        )
      }
    }

    // Add Habit FAB
    FloatingActionButton(
      onClick = onAddHabitClicked,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("add_habit_fab"),
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = Color.White,
      shape = RoundedCornerShape(18.dp)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Habit")
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isFa) "عادت جدید" else "New Habit",
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun HabitSummaryHeader(
  completedCount: Int,
  totalCount: Int,
  isFa: Boolean,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFF97316).copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = null,
            tint = Color(0xFFF97316),
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = if (isFa) "استمرار و تداوم روزانه" else "Daily Consistency",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (isFa) {
              "$completedCount از $totalCount عادت امروز تکمیل شده"
            } else {
              "$completedCount of $totalCount completed today"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun HabitItemCard(
  habit: HabitItem,
  isFa: Boolean,
  onToggleToday: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("habit_item_${habit.id}"),
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = habit.title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Category badge
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(habit.colorHex).copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = habit.category,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(habit.colorHex),
                fontSize = 11.sp
              )
            }
          }

          if (habit.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = habit.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Action Check Button
        Surface(
          onClick = onToggleToday,
          shape = CircleShape,
          color = if (habit.isCompletedToday) Color(0xFF10B981) else MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.size(44.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            if (habit.isCompletedToday) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed Today",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            } else {
              Text(
                text = if (isFa) "ثبت" else "Done",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Progress bar and streak
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = null,
            tint = Color(0xFFF97316),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isFa) "${habit.streakDays} روز متوالی" else "${habit.streakDays} day streak",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFF97316)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (isFa) {
              "هدف: ${habit.targetDaysPerWeek} روز/هفته"
            } else {
              "Target: ${habit.targetDaysPerWeek} d/w"
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete Habit",
              tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}
