package data

import androidx.compose.runtime.Immutable
import state.DataCategory

@Immutable
data class Recipe(
    override val id: String,
    override val name: String = "",
    val recipeType: String = "",
    val duration: UInt = 0u,
    val itemInputs: List<ItemInputStack> = emptyList(),
    val fluidInputs: List<FluidStack> = emptyList(),
    val itemOutputs: List<ItemOutputStack> = emptyList(),
    val fluidOutputs: List<FluidStack> = emptyList(),
) : DataItem {
    override val category: DataCategory
        get() = DataCategory.Recipes

    override fun renamed(id: String, name: String): Recipe = copy(id = id, name = name)
}

@Immutable
data class ItemInputStack(val itemId: String, val amount: UInt)

@Immutable
data class ItemOutputStack(val itemId: String, val amount: UInt, val chance: UInt)

@Immutable
data class FluidStack(val fluidId: String, val amount: UInt)