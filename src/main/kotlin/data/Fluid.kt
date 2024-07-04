package data

import androidx.compose.runtime.Immutable
import state.DataCategory

@Immutable
data class Fluid(
    override val id: String,
    override val name: String = "",
) : DataItem {
    override val category: DataCategory
        get() = DataCategory.Fluids

    override fun renamed(id: String, name: String): Fluid = copy(id = id, name = name)
}