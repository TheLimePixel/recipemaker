package data

import androidx.compose.runtime.Immutable
import state.DataCategory

@Immutable
data class RecipeType(
    override val id: String,
    override val name: String = "",
    val maxItemInputs: UInt = 0u,
    val maxFluidInputs: UInt = 0u,
    val maxItemOutputs: UInt = 0u,
    val maxFluidOutputs: UInt = 0u,
    val inheritsFrom: String = "",
) : DataItem {
    override val category: DataCategory
        get() = DataCategory.RecipeTypes

    override fun renamed(id: String, name: String): RecipeType = copy(id = id, name = name)
}