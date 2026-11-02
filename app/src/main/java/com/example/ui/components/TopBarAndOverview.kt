package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.KarinoUiState

@Composable
fun KarinoTopBar(
  state: KarinoUiState,
  onToggleLanguage: () -> Unit,
  onToggleTheme: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isFa = state.language == AppLanguage.PERSIAN
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // App Logo & Title
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(
            Brush.linearGradient(
              listOf(
                Color(0xFF6366F1),
                Color(0xFF06B6D4)
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = if (isFa) "کارینو" else "Karino",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = if (isFa) 0.sp else 1.sp
          ),
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = if (isFa) "برنامه‌ریز و دستیار روزانه" else "Daily Planner & Habit Hub",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Actions: Language Toggle & Theme Toggle
    Row(verticalAlignment = Alignment.CenterVertically) {
      // Language chip button
      Surface(
        onClick = onToggleLanguage,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
          .testTag("lang_toggle_button")
          .height(36.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Translate,
            contentDescription = "Language",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isFa) "FA | فا" else "EN",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Theme toggle button
      IconButton(
        onClick = onToggleTheme,
        modifier = Modifier
          .testTag("theme_toggle_button")
          .size(38.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Icon(
          imageVector = if (state.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
          contentDescription = "Toggle Theme",
          tint = if (state.isDarkTheme) Color(0xFFFBBF24) else MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun OverviewCard(
  state: KarinoUiState,
  modifier: Modifier = Modifier
) {
  val isFa = state.language == AppLanguage.PERSIAN
  val progressAnimated by animateFloatAsState(
    targetValue = if (state.totalTasksCount > 0) state.completedTasksCount.toFloat() / state.totalTasksCount else 0f,
    label = "progress"
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
  ) {
    Column(
      modifier = Modifier
        .background(
          Brush.verticalGradient(
            colors = listOf(
              MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
              Color.Transparent
            )
          )
        )
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (isFa) "وضعیت کارهای امروز" else "Today's Progress",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isFa) {
              "${state.completedTasksCount} از ${state.totalTasksCount} کار تکمیل شده"
            } else {
              "${state.completedTasksCount} of ${state.totalTasksCount} tasks completed"
            },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        // Circular progress indicator with center percentage
        Box(contentAlignment = Alignment.Center) {
          CircularProgressIndicator(
            progress = { progressAnimated },
            modifier = Modifier.size(56.dp),
            strokeWidth = 6.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
          Text(
            text = "${(progressAnimated * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Metric mini chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MiniStatChip(
          icon = Icons.Default.CheckCircle,
          label = if (isFa) "تکمیل کارها" else "Done",
          value = "${state.completedTasksCount}/${state.totalTasksCount}",
          accentColor = Color(0xFF10B981),
          modifier = Modifier.weight(1f)
        )
        MiniStatChip(
          icon = Icons.Default.LocalFireDepartment,
          label = if (isFa) "عادت‌های امروز" else "Habits",
          value = "${state.completedHabitsToday}/${state.totalHabitsCount}",
          accentColor = Color(0xFFF97316),
          modifier = Modifier.weight(1f)
        )
        MiniStatChip(
          icon = Icons.Default.StickyNote2,
          label = if (isFa) "یادداشت‌ها" else "Notes",
          value = "${state.notes.size}",
          accentColor = Color(0xFF06B6D4),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Daily quote banner
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.FormatQuote,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isFa) {
            "«تداوم در گام‌های کوچک، آینده را می‌سازد.»"
          } else {
            "\"Small daily improvements over time lead to stunning results.\""
          },
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun MiniStatChip(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = accentColor,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 10.sp
        )
        Text(
          text = value,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}
