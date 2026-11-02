package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Priority
import com.example.model.TaskCategory
import com.example.model.TaskItem
import com.example.ui.KarinoUiState

@Composable
fun TasksScreen(
  state: KarinoUiState,
  onSearchQueryChanged: (String) -> Unit,
  onCategorySelected: (TaskCategory?) -> Unit,
  onToggleTaskCompletion: (String) -> Unit,
  onDeleteTask: (String) -> Unit,
  onAddTaskClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isFa = state.language == AppLanguage.PERSIAN

  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Search Box
      OutlinedTextField(
        value = state.searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .testTag("task_search_input"),
        placeholder = {
          Text(
            text = if (isFa) "جستجو در کارها و وظایف..." else "Search tasks...",
            style = MaterialTheme.typography.bodyMedium
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        },
        trailingIcon = {
          if (state.searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChanged("") }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
          focusedBorderColor = MaterialTheme.colorScheme.primary
        )
      )

      // Category filter chips
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          FilterChip(
            selected = state.selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = {
              Text(
                text = if (isFa) "همه (${state.allTasks.size})" else "All (${state.allTasks.size})",
                fontWeight = if (state.selectedCategory == null) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
          )
        }

        items(TaskCategory.values()) { category ->
          val isSelected = state.selectedCategory == category
          val count = state.allTasks.count { it.category == category }
          FilterChip(
            selected = isSelected,
            onClick = { onCategorySelected(if (isSelected) null else category) },
            label = {
              Text(
                text = "${if (isFa) category.titleFa else category.titleEn} ($count)",
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            leadingIcon = {
              Icon(
                imageVector = getCategoryIcon(category),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = Color.White,
              selectedLeadingIconColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
          )
        }
      }

      // Tasks List or Empty State
      if (state.filteredTasks.isEmpty()) {
        EmptyTasksState(isFa = isFa, onAddTaskClicked = onAddTaskClicked)
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(
            items = state.filteredTasks,
            key = { it.id }
          ) { task ->
            TaskItemCard(
              task = task,
              isFa = isFa,
              onToggleCompletion = { onToggleTaskCompletion(task.id) },
              onDelete = { onDeleteTask(task.id) }
            )
          }
        }
      }
    }

    // Add Task FAB
    FloatingActionButton(
      onClick = onAddTaskClicked,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("add_task_fab"),
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = Color.White,
      shape = RoundedCornerShape(18.dp)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Task")
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isFa) "کار جدید" else "New Task",
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun TaskItemCard(
  task: TaskItem,
  isFa: Boolean,
  onToggleCompletion: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("task_item_${task.id}"),
    shape = RoundedCornerShape(16.dp),
    color = if (task.isCompleted) {
      MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    } else {
      MaterialTheme.colorScheme.surface
    },
    tonalElevation = if (task.isCompleted) 0.dp else 2.dp,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (task.isCompleted) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onToggleCompletion() }
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Checkbox circle
      Box(
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(
            if (task.isCompleted) Color(0xFF10B981) else Color.Transparent
          )
          .border(
            2.dp,
            if (task.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.outline,
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (task.isCompleted) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Completed",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Content
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
          ),
          color = if (task.isCompleted) {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          } else {
            MaterialTheme.colorScheme.onSurface
          }
        )

        if (task.description.isNotBlank()) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = task.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Badges: Category, Priority, Due Date
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Priority badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(task.priority.colorHex).copy(alpha = 0.15f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = if (isFa) task.priority.titleFa else task.priority.titleEn,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color(task.priority.colorHex),
              fontSize = 11.sp
            )
          }

          // Category badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = if (isFa) task.category.titleFa else task.category.titleEn,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          }

          // Due date
          if (task.dueDate.isNotBlank()) {
            Text(
              text = "• ${task.dueDate}",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          }
        }
      }

      // Delete action
      IconButton(
        onClick = onDelete,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Delete",
          tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun EmptyTasksState(
  isFa: Boolean,
  onAddTaskClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.TaskAlt,
        contentDescription = null,
        modifier = Modifier.size(44.dp),
        tint = MaterialTheme.colorScheme.primary
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = if (isFa) "هیچ کاری یافت نشد!" else "No tasks found!",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = if (isFa) {
        "کارهای روزانه‌تان را اضافه کنید تا با نظم و تمرکز بیشتری پیش بروید."
      } else {
        "Add your daily tasks to stay focused and productive."
      },
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

fun getCategoryIcon(category: TaskCategory): androidx.compose.ui.graphics.vector.ImageVector {
  return when (category) {
    TaskCategory.WORK -> Icons.Default.Work
    TaskCategory.PERSONAL -> Icons.Default.Person
    TaskCategory.SHOPPING -> Icons.Default.ShoppingCart
    TaskCategory.STUDY -> Icons.Default.School
    TaskCategory.FITNESS -> Icons.Default.FitnessCenter
  }
}
