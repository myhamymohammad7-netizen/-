package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.TaskCategory
import com.example.ui.KarinoUiState

@Composable
fun StatsScreen(
  state: KarinoUiState,
  modifier: Modifier = Modifier
) {
  val isFa = state.language == AppLanguage.PERSIAN

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Score Card
    item {
      OverallScoreCard(state = state, isFa = isFa)
    }

    // Weekly Productivity Bar Chart
    item {
      WeeklyActivityChartCard(isFa = isFa)
    }

    // Category Distribution Card
    item {
      CategoryBreakdownCard(state = state, isFa = isFa)
    }
  }
}

@Composable
fun OverallScoreCard(
  state: KarinoUiState,
  isFa: Boolean,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0xFF6366F1).copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoGraph,
            contentDescription = null,
            tint = Color(0xFF6366F1),
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = if (isFa) "شاخص بهره‌وری امروز" else "Today's Productivity Score",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (isFa) "محاسبه بر اساس کارها و عادت‌ها" else "Calculated from completed tasks & habits",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        MetricScoreBox(
          title = if (isFa) "نرخ تکمیل کارها" else "Task Rate",
          value = "${state.taskCompletionPercentage}%",
          color = Color(0xFF10B981)
        )
        MetricScoreBox(
          title = if (isFa) "عادت‌های موفق" else "Habits Met",
          value = "${state.completedHabitsToday}/${state.totalHabitsCount}",
          color = Color(0xFF06B6D4)
        )
        MetricScoreBox(
          title = if (isFa) "یادداشت‌ها" else "Saved Notes",
          value = "${state.notes.size}",
          color = Color(0xFFF59E0B)
        )
      }
    }
  }
}

@Composable
fun MetricScoreBox(
  title: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
  ) {
    Text(
      text = value,
      style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
      color = color
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun WeeklyActivityChartCard(
  isFa: Boolean,
  modifier: Modifier = Modifier
) {
  val daysFa = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")
  val daysEn = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
  val heights = listOf(0.45f, 0.75f, 0.60f, 0.90f, 0.85f, 0.50f, 0.30f)
  val primaryColor = MaterialTheme.colorScheme.primary
  val trackColor = MaterialTheme.colorScheme.surfaceVariant

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text(
        text = if (isFa) "روند عملکرد ۷ روز گذشته" else "7-Day Performance Trend",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = if (isFa) "میزان فعالیت و بستن اهداف در طول هفته" else "Activity and goal completion across days",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Canvas Bar Chart
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(120.dp)
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val barCount = heights.size
          val totalSpacing = size.width * 0.3f
          val barWidth = (size.width - totalSpacing) / barCount
          val gap = totalSpacing / (barCount + 1)

          for (i in 0 until barCount) {
            val left = gap + i * (barWidth + gap)
            val barHeight = size.height * heights[i]
            val top = size.height - barHeight

            // Draw track background
            drawRoundRect(
              color = trackColor,
              topLeft = Offset(left, 0f),
              size = Size(barWidth, size.height),
              cornerRadius = CornerRadius(8f, 8f)
            )

            // Draw filled bar
            drawRoundRect(
              color = primaryColor,
              topLeft = Offset(left, top),
              size = Size(barWidth, barHeight),
              cornerRadius = CornerRadius(8f, 8f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Day labels
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val days = if (isFa) daysFa else daysEn
        days.forEach { day ->
          Text(
            text = day,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(36.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    }
  }
}

@Composable
fun CategoryBreakdownCard(
  state: KarinoUiState,
  isFa: Boolean,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.PieChart,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isFa) "توزیع کارها بر اساس دسته‌بندی" else "Tasks by Category",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      val total = if (state.totalTasksCount == 0) 1 else state.totalTasksCount

      TaskCategory.values().forEach { category ->
        val count = state.allTasks.count { it.category == category }
        val fraction = count.toFloat() / total
        val label = if (isFa) category.titleFa else category.titleEn

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = label,
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "$count (${(fraction * 100).toInt()}%)",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
        }
      }
    }
  }
}
