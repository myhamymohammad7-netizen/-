package com.example.data

import com.example.model.HabitItem
import com.example.model.NoteItem
import com.example.model.Priority
import com.example.model.TaskCategory
import com.example.model.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class KarinoRepository {

  private val _tasks = MutableStateFlow<List<TaskItem>>(initialTasks())
  val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

  private val _habits = MutableStateFlow<List<HabitItem>>(initialHabits())
  val habits: StateFlow<List<HabitItem>> = _habits.asStateFlow()

  private val _notes = MutableStateFlow<List<NoteItem>>(initialNotes())
  val notes: StateFlow<List<NoteItem>> = _notes.asStateFlow()

  // Task actions
  fun addTask(
    title: String,
    description: String,
    category: TaskCategory,
    priority: Priority,
    dueDate: String
  ) {
    val newTask = TaskItem(
      id = UUID.randomUUID().toString(),
      title = title,
      description = description,
      category = category,
      priority = priority,
      dueDate = dueDate.ifBlank { "امروز" },
      isCompleted = false
    )
    _tasks.value = listOf(newTask) + _tasks.value
  }

  fun toggleTaskCompletion(id: String) {
    _tasks.value = _tasks.value.map { task ->
      if (task.id == id) task.copy(isCompleted = !task.isCompleted) else task
    }
  }

  fun deleteTask(id: String) {
    _tasks.value = _tasks.value.filter { it.id != id }
  }

  // Habit actions
  fun toggleHabitToday(id: String) {
    _habits.value = _habits.value.map { habit ->
      if (habit.id == id) {
        val willBeDone = !habit.isCompletedToday
        val newStreak = if (willBeDone) habit.streakDays + 1 else maxOf(0, habit.streakDays - 1)
        habit.copy(isCompletedToday = willBeDone, streakDays = newStreak)
      } else {
        habit
      }
    }
  }

  fun addHabit(title: String, description: String, category: String, targetDays: Int) {
    val newHabit = HabitItem(
      id = UUID.randomUUID().toString(),
      title = title,
      description = description,
      streakDays = 1,
      isCompletedToday = false,
      targetDaysPerWeek = targetDays,
      category = category
    )
    _habits.value = _habits.value + newHabit
  }

  fun deleteHabit(id: String) {
    _habits.value = _habits.value.filter { it.id != id }
  }

  // Note actions
  fun addNote(title: String, content: String, colorIndex: Int) {
    val newNote = NoteItem(
      id = UUID.randomUUID().toString(),
      title = title,
      content = content,
      colorIndex = colorIndex,
      isPinned = false
    )
    _notes.value = listOf(newNote) + _notes.value
  }

  fun togglePinNote(id: String) {
    _notes.value = _notes.value.map { note ->
      if (note.id == id) note.copy(isPinned = !note.isPinned) else note
    }.sortedByDescending { it.isPinned }
  }

  fun deleteNote(id: String) {
    _notes.value = _notes.value.filter { it.id != id }
  }

  companion object {
    private fun initialTasks(): List<TaskItem> = listOf(
      TaskItem(
        id = "1",
        title = "آماده‌سازی گزارش هفتگی پروژه",
        description = "بررسی شاخص‌های عملکرد و تحلیل نتایج تیم برای جلسه پنجشنبه",
        category = TaskCategory.WORK,
        priority = Priority.HIGH,
        isCompleted = false,
        dueDate = "امروز"
      ),
      TaskItem(
        id = "2",
        title = "مطالعه ۳۰ صفحه از کتاب توسعه فردی",
        description = "فصل چهارم کتاب عادت‌های اتمی جیمز کلیر",
        category = TaskCategory.STUDY,
        priority = Priority.MEDIUM,
        isCompleted = true,
        dueDate = "امروز"
      ),
      TaskItem(
        id = "3",
        title = "خرید وسایل خانه و خواروبار",
        description = "شیر، میوه، روغن زیتون، نان سنگک تازه",
        category = TaskCategory.SHOPPING,
        priority = Priority.LOW,
        isCompleted = false,
        dueDate = "فردا"
      ),
      TaskItem(
        id = "4",
        title = "تمرین دویدن یا پیاده‌روی سریع",
        description = "حداقل ۵ کیلومتر در هوای آزاد یا تردمیل",
        category = TaskCategory.FITNESS,
        priority = Priority.MEDIUM,
        isCompleted = false,
        dueDate = "امروز"
      ),
      TaskItem(
        id = "5",
        title = "تنظیم و بازبینی بودجه ماهانه",
        description = "ثبت هزینه‌های جاری و پس‌انداز ماه جدید",
        category = TaskCategory.PERSONAL,
        priority = Priority.HIGH,
        isCompleted = true,
        dueDate = "دیروز"
      )
    )

    private fun initialHabits(): List<HabitItem> = listOf(
      HabitItem(
        id = "h1",
        title = "نوشیدن ۸ لیوان آب",
        description = "حفظ آب‌رسانی و شادابی بدن در طول روز",
        streakDays = 12,
        isCompletedToday = true,
        targetDaysPerWeek = 7,
        category = "سلامت",
        colorHex = 0xFF06B6D4
      ),
      HabitItem(
        id = "h2",
        title = "۳۰ دقیقه مطالعه روزانه",
        description = "یادگیری مستمر و گسترش دایره دانش",
        streakDays = 8,
        isCompletedToday = true,
        targetDaysPerWeek = 6,
        category = "رشد فردی",
        colorHex = 0xFF6366F1
      ),
      HabitItem(
        id = "h3",
        title = "تمرین تمرکز و مدیتیشن",
        description = "۱۰ دقیقه آرامش ذهن و تنفس آگاهانه",
        streakDays = 5,
        isCompletedToday = false,
        targetDaysPerWeek = 5,
        category = "ذهن‌آگاهی",
        colorHex = 0xFF10B981
      ),
      HabitItem(
        id = "h4",
        title = "خواب قبل از ساعت ۱۲ شب",
        description = "تنظیم ساعت زیستی بدن و استراحت کافی",
        streakDays = 4,
        isCompletedToday = false,
        targetDaysPerWeek = 7,
        category = "سلامت",
        colorHex = 0xFFF59E0B
      )
    )

    private fun initialNotes(): List<NoteItem> = listOf(
      NoteItem(
        id = "n1",
        title = "ایده‌های کلیدی بهبود عملکرد",
        content = "۱. قانون ۲ دقیقه: کارهایی که زیر دو دقیقه طول می‌کشند را فوراً انجام بده.\n۲. اولویت‌بندی بر اساس ماتریس آیزنهاور.\n۳. زمان‌بندی بلوک‌های تمرکز عمیق (دیپ ورک).",
        colorIndex = 0,
        isPinned = true
      ),
      NoteItem(
        id = "n2",
        title = "اهداف سه‌ماهه پاییز",
        content = "- تسلط بر معماری پیشرفته نرم‌افزار\n- پیاده‌سازی پروژه جدید شخصی\n- بهبود آمادگی جسمانی و ورزش منظم ۳ روز در هفته",
        colorIndex = 1,
        isPinned = true
      ),
      NoteItem(
        id = "n3",
        title = "نقل‌قول انگیزشی امروز",
        content = "«تکرار کارهای کوچک روزمره است که نتایج خارق‌العاده و دگرگون‌کننده را می‌سازد.»",
        colorIndex = 2,
        isPinned = false
      )
    )
  }
}
