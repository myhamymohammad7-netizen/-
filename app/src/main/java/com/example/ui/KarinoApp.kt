package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.model.AppTab
import com.example.ui.components.KarinoTopBar
import com.example.ui.components.OverviewCard
import com.example.ui.dialogs.AddHabitDialog
import com.example.ui.dialogs.AddNoteDialog
import com.example.ui.dialogs.AddTaskDialog
import com.example.ui.screens.HabitsScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.theme.KarinoTheme

@Composable
fun KarinoApp(
  viewModel: KarinoViewModel = viewModel()
) {
  val state by viewModel.uiState.collectAsState()
  val isFa = state.language == AppLanguage.PERSIAN
  val layoutDirection = if (isFa) LayoutDirection.Rtl else LayoutDirection.Ltr

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    KarinoTheme(darkTheme = state.isDarkTheme) {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
          KarinoBottomNavigationBar(
            currentTab = state.currentTab,
            onTabSelected = { viewModel.selectTab(it) },
            isFa = isFa,
            modifier = Modifier.testTag("bottom_nav_bar")
          )
        },
        containerColor = MaterialTheme.colorScheme.background
      ) { innerPadding ->
        Column(
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
          // Top Header Bar
          KarinoTopBar(
            state = state,
            onToggleLanguage = { viewModel.toggleLanguage() },
            onToggleTheme = { viewModel.toggleTheme() }
          )

          // Daily Progress Summary Card (show on tasks and habits tabs)
          if (state.currentTab == AppTab.TASKS || state.currentTab == AppTab.HABITS) {
            OverviewCard(state = state)
          }

          // Content Pane with smooth crossfade animation
          Box(modifier = Modifier.weight(1f)) {
            Crossfade(targetState = state.currentTab, label = "tab_fade") { tab ->
              when (tab) {
                AppTab.TASKS -> TasksScreen(
                  state = state,
                  onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                  onCategorySelected = { viewModel.selectCategory(it) },
                  onToggleTaskCompletion = { viewModel.toggleTaskCompletion(it) },
                  onDeleteTask = { viewModel.deleteTask(it) },
                  onAddTaskClicked = { viewModel.setAddTaskDialogVisible(true) }
                )
                AppTab.HABITS -> HabitsScreen(
                  state = state,
                  onToggleHabitToday = { viewModel.toggleHabitToday(it) },
                  onDeleteHabit = { viewModel.deleteHabit(it) },
                  onAddHabitClicked = { viewModel.setAddHabitDialogVisible(true) }
                )
                AppTab.NOTES -> NotesScreen(
                  state = state,
                  onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                  onTogglePinNote = { viewModel.togglePinNote(it) },
                  onDeleteNote = { viewModel.deleteNote(it) },
                  onAddNoteClicked = { viewModel.setAddNoteDialogVisible(true) }
                )
                AppTab.STATS -> StatsScreen(
                  state = state
                )
              }
            }
          }
        }

        // Dialogs
        if (state.showAddTaskDialog) {
          AddTaskDialog(
            isFa = isFa,
            onDismiss = { viewModel.setAddTaskDialogVisible(false) },
            onConfirm = { title, desc, cat, prio, due ->
              viewModel.addTask(title, desc, cat, prio, due)
            }
          )
        }

        if (state.showAddHabitDialog) {
          AddHabitDialog(
            isFa = isFa,
            onDismiss = { viewModel.setAddHabitDialogVisible(false) },
            onConfirm = { title, desc, cat, target ->
              viewModel.addHabit(title, desc, cat, target)
            }
          )
        }

        if (state.showAddNoteDialog) {
          AddNoteDialog(
            isFa = isFa,
            onDismiss = { viewModel.setAddNoteDialogVisible(false) },
            onConfirm = { title, content, colorIdx ->
              viewModel.addNote(title, content, colorIdx)
            }
          )
        }
      }
    }
  }
}

@Composable
fun KarinoBottomNavigationBar(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  isFa: Boolean,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.navigationBarsPadding(),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    AppTab.values().forEach { tab ->
      val selected = currentTab == tab
      NavigationBarItem(
        selected = selected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = getTabIcon(tab, selected),
            contentDescription = if (isFa) tab.titleFa else tab.titleEn
          )
        },
        label = {
          Text(
            text = if (isFa) tab.titleFa else tab.titleEn,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.primary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          indicatorColor = MaterialTheme.colorScheme.primaryContainer,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}

fun getTabIcon(tab: AppTab, isSelected: Boolean): androidx.compose.ui.graphics.vector.ImageVector {
  return when (tab) {
    AppTab.TASKS -> if (isSelected) Icons.Default.FormatListBulleted else Icons.Outlined.FormatListBulleted
    AppTab.HABITS -> if (isSelected) Icons.Default.LocalFireDepartment else Icons.Outlined.LocalFireDepartment
    AppTab.NOTES -> if (isSelected) Icons.Default.StickyNote2 else Icons.Outlined.StickyNote2
    AppTab.STATS -> if (isSelected) Icons.Default.AutoGraph else Icons.Outlined.AutoGraph
  }
}
