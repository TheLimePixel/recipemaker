package state

import androidx.compose.runtime.*
import data.DataItem

@Immutable
data class DataItemState<out T : DataItem>(val isNew: Boolean, val old: T, val current: T?) {
    val latest: T get() = current ?: old
    val id: String get() = old.id
}