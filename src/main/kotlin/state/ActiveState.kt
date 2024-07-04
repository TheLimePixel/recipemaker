package state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
class ActiveState {
    val id = mutableStateOf<String?>(null)
    val category = mutableStateOf(DataCategory.Items)
    val deleting = mutableStateOf(false)
}