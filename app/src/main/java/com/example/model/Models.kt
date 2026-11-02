package com.example.model

enum class Priority(val titleFa: String, val titleEn: String, val colorHex: Long) {
  HIGH("فوری و مهم", "High", 0xFFEF4444),
  MEDIUM("متوسط", "Medium", 0xFFF59E0B),
  LOW("معمولی", "Low", 0xFF10B981)
}

enum class TaskCategory(val titleFa: String, val titleEn: String, val iconName: String) {
  WORK("کاری", "Work", "work"),
  PERSONAL("شخصی", "Personal", "person"),
  SHOPPING("خرید", "Shopping", "shopping_cart"),
  STUDY("آموزش", "Study", "school"),
  FITNESS("سلامت", "Health", "fitness_center")
}

data class TaskItem(
  val id: String,
  val title: String,
  val description: String = "",
  val category: TaskCategory = TaskCategory.PERSONAL,
  val priority: Priority = Priority.MEDIUM,
  val isCompleted: Boolean = false,
  val dueDate: String = "امروز",
  val createdAt: Long = System.currentTimeMillis()
)

data class HabitItem(
  val id: String,
  val title: String,
  val description: String = "",
  val streakDays: Int = 0,
  val isCompletedToday: Boolean = false,
  val targetDaysPerWeek: Int = 7,
  val category: String = "سبک زندگی",
  val colorHex: Long = 0xFF6366F1
)

data class NoteItem(
  val id: String,
  val title: String,
  val content: String,
  val colorIndex: Int = 0,
  val isPinned: Boolean = false,
  val updatedAt: Long = System.currentTimeMillis()
)

enum class AppTab(val titleFa: String, val titleEn: String) {
  TASKS("کارها", "Tasks"),
  HABITS("عادت‌ها", "Habits"),
  NOTES("یادداشت", "Notes"),
  STATS("آمار", "Insights")
}

enum class AppLanguage {
  PERSIAN,
  ENGLISH
}
