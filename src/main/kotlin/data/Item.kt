package data

import androidx.compose.runtime.Immutable
import state.DataCategory

@Immutable
data class Item(
    override val id: String,
    override val name: String = "",
    val recipeType: String = "",
) : DataItem {
    override val category: DataCategory
        get() = DataCategory.Items

    override fun renamed(id: String, name: String): Item = copy(id = id, name = name)
}