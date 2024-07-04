package data

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import state.DataCategory

@Immutable
sealed interface DataItem {
    val id: String
    val name: String
    val category: DataCategory

    val trueName: String
        get() = name.ifEmpty { formatId(id) }

    fun renamed(id: String, name: String): DataItem
}

private fun formatId(id: String) = id
    .split("_")
    .filter { it.isNotEmpty() }
    .joinToString(" ") { it.capitalize(Locale.current) }