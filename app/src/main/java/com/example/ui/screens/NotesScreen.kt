package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.NoteItem
import com.example.ui.KarinoUiState
import com.example.ui.theme.NoteColor1
import com.example.ui.theme.NoteColor2
import com.example.ui.theme.NoteColor3
import com.example.ui.theme.NoteColor4
import com.example.ui.theme.NoteColor5

@Composable
fun NotesScreen(
  state: KarinoUiState,
  onSearchQueryChanged: (String) -> Unit,
  onTogglePinNote: (String) -> Unit,
  onDeleteNote: (String) -> Unit,
  onAddNoteClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isFa = state.language == AppLanguage.PERSIAN

  val noteBackgrounds = listOf(
    NoteColor1, NoteColor2, NoteColor3, NoteColor4, NoteColor5
  )

  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Search box for notes
      OutlinedTextField(
        value = state.searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .testTag("notes_search_input"),
        placeholder = {
          Text(
            text = if (isFa) "جستجو در یادداشت‌ها و ایده‌ها..." else "Search notes & ideas...",
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

      if (state.notes.isEmpty()) {
        EmptyNotesState(isFa = isFa)
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(
            items = state.notes,
            key = { it.id }
          ) { note ->
            val bg = noteBackgrounds[note.colorIndex % noteBackgrounds.size]
            NoteCard(
              note = note,
              cardBgColor = if (state.isDarkTheme) bg.copy(alpha = 0.22f) else bg,
              onTogglePin = { onTogglePinNote(note.id) },
              onDelete = { onDeleteNote(note.id) }
            )
          }
        }
      }
    }

    // Add Note FAB
    FloatingActionButton(
      onClick = onAddNoteClicked,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("add_note_fab"),
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = Color.White,
      shape = RoundedCornerShape(18.dp)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Note")
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isFa) "یادداشت جدید" else "New Note",
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun NoteCard(
  note: NoteItem,
  cardBgColor: Color,
  onTogglePin: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("note_card_${note.id}"),
    shape = RoundedCornerShape(16.dp),
    color = cardBgColor,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (note.isPinned) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent
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
        Text(
          text = note.title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onTogglePin,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = if (note.isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin,
              contentDescription = "Pin Note",
              tint = if (note.isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }

          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete Note",
              tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = note.content,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
        lineHeight = 22.sp
      )
    }
  }
}

@Composable
fun EmptyNotesState(
  isFa: Boolean,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = if (isFa) "هیچ یادداشتی وجود ندارد!" else "No notes found!",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = if (isFa) "ایده‌ها و نکات مهم خود را ثبت کنید." else "Capture your thoughts and bright ideas.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
