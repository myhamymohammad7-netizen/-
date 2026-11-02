package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.KarinoRepository
import com.example.model.AppLanguage
import com.example.model.AppTab
import com.example.model.HabitItem
import com.example.model.NoteItem
import com.example.model.Priority
import com.example.model.TaskCategory
import com.example.model.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class KarinoUiState(
  val currentTab: AppTab = AppTab.TASKS,
  val selectedCategory: TaskCategory? = null,
  val searchQuery: String = "",
  val language: AppLanguage = AppLanguage.PERSIAN,
  val isDarkTheme: Boolean = true,
  val allTasks: List<TaskItem> = emptyList(),
  val filteredTasks: List<TaskItem> = emptyList(),
  val habits: List<HabitItem> = emptyList(),
  val notes: List<NoteItem> = emptyList(),
  val showAddTaskDialog: Boolean = false,
  val showAddHabitDialog: Boolean = false,
  val showAddNoteDialog: Boolean = false
) {
  val totalTasksCount: Int get() = allTasks.size
  val completedTasksCount: Int get() = allTasks.count { it.isCompleted }
  val taskCompletionPercentage: Int
    get() = if (totalTasksCount == 0) 0 else ((completedTasksCount * 100f) / totalTasksCount).toInt()

  val completedHabitsToday: Int get() = habits.count { it.isCompletedToday }
  val totalHabitsCount: Int get() = habits.size
}

class KarinoViewModel(
  private val repository: KarinoRepository = KarinoRepository()
) : ViewModel() {

  private val _currentTab = MutableStateFlow(AppTab.TASKS)
  private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)
  private val _searchQuery = MutableStateFlow("")
  private val _language = MutableStateFlow(AppLanguage.PERSIAN)
  private val _isDarkTheme = MutableStateFlow(true)
  private val _showAddTaskDialog = MutableStateFlow(false)
  private val _showAddHabitDialog = MutableStateFlow(false)
  private val _showAddNoteDialog = MutableStateFlow(false)

  val uiState: StateFlow<KarinoUiState> = combine(
    repository.tasks,
    repository.habits,
    repository.notes,
    _currentTab,
    _selectedCategory,
    _searchQuery,
    _language,
    _isDarkTheme,
    _showAddTaskDialog,
    _showAddHabitDialog,
    _showAddNoteDialog
  ) { args ->
    val tasks = args[0] as List<TaskItem>
    val habits = args[1] as List<HabitItem>
    val notes = args[2] as List<NoteItem>
    val currentTab = args[3] as AppTab
    val selectedCategory = args[4] as TaskCategory?
    val searchQuery = args[5] as String
    val language = args[6] as AppLanguage
    val isDarkTheme = args[7] as Boolean
    val showAddTask = args[8] as Boolean
    val showAddHabit = args[9] as Boolean
    val showAddNote = args[10] as Boolean

    val filteredTasks = tasks.filter { task ->
      val matchesCategory = selectedCategory == null || task.category == selectedCategory
      val matchesQuery = searchQuery.isBlank() ||
          task.title.contains(searchQuery, ignoreCase = true) ||
          task.description.contains(searchQuery, ignoreCase = true)
      matchesCategory && matchesQuery
    }

    val filteredNotes = notes.filter { note ->
      searchQuery.isBlank() ||
          note.title.contains(searchQuery, ignoreCase = true) ||
          note.content.contains(searchQuery, ignoreCase = true)
    }

    KarinoUiState(
      currentTab = currentTab,
      selectedCategory = selectedCategory,
      searchQuery = searchQuery,
      language = language,
      isDarkTheme = isDarkTheme,
      allTasks = tasks,
      filteredTasks = filteredTasks,
      habits = habits,
      notes = filteredNotes,
      showAddTaskDialog = showAddTask,
      showAddHabitDialog = showAddHabit,
      showAddNoteDialog = showAddNote
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = KarinoUiState()
  )

  fun selectTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun selectCategory(category: TaskCategory?) {
    _selectedCategory.value = category
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun toggleLanguage() {
    _language.value = if (_language.value == AppLanguage.PERSIAN) AppLanguage.ENGLISH else AppLanguage.PERSIAN
  }

  fun toggleTheme() {
    _isDarkTheme.value = !_isDarkTheme.value
  }

  // Dialog controls
  fun setAddTaskDialogVisible(visible: Boolean) {
    _showAddTaskDialog.value = visible
  }

  fun setAddHabitDialogVisible(visible: Boolean) {
    _showAddHabitDialog.value = visible
  }

  fun setAddNoteDialogVisible(visible: Boolean) {
    _showAddNoteDialog.value = visible
  }

  // Task actions
  fun toggleTaskCompletion(id: String) {
    repository.toggleTaskCompletion(id)
  }

  fun addTask(title: String, description: String, category: TaskCategory, priority: Priority, dueDate: String) {
    repository.addTask(title, description, category, priority, dueDate)
    _showAddTaskDialog.value = false
  }

  fun deleteTask(id: String) {
    repository.deleteTask(id)
  }

  // Habit actions
  fun toggleHabitToday(id: String) {
    repository.toggleHabitToday(id)
  }

  fun addHabit(title: String, description: String, category: String, targetDays: Int) {
    repository.addHabit(title, description, category, targetDays)
    _showAddHabitDialog.value = false
  }

  fun deleteHabit(id: String) {
    repository.deleteHabit(id)
  }

  // Note actions
  fun addNote(title: String, content: String, colorIndex: Int) {
    repository.addNote(title, content, colorIndex)
    _showAddNoteDialog.value = false
  }

  fun togglePinNote(id: String) {
    repository.togglePinNote(id)
  }

  fun deleteNote(id: String) {
    repository.deleteNote(id)
  }
}
