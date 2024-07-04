package components.views.mutable

import androidx.compose.runtime.Composable
import data.FluidStack
import data.ItemInputStack
import data.ItemOutputStack
import data.Recipe
import state.DataManager

@Composable
fun MutableRecipeView(
    id: String,
    recipe: Recipe,
    isNew: Boolean,
    manager: DataManager,
) = MutableDataView(id, recipe, isNew, manager) {
    val dataset = manager.primary

    MutableDataField("Recipe Type", dataset.getRecipeType(recipe.recipeType)?.old, dataset.allRecipes) { type ->
        manager.updateEdit(id, recipe.copy(recipeType = type))
    }

    MutableDurationField(recipe.duration) {
        manager.updateEdit(id, recipe.copy(duration = it))
    }

    MutableLabeledList(
        "Item Inputs",
        recipe.itemInputs.toTypedArray(),
        { manager.updateEdit(id, recipe.copy(itemInputs = it)) },
        ItemInputStack("", 1u)
    ) { value, updateValue ->
        MutableItemInputRow(value, dataset::getItem, dataset.allItems, updateValue)
    }

    MutableLabeledList(
        "Fluid Inputs",
        recipe.fluidInputs.toTypedArray(),
        { manager.updateEdit(id, recipe.copy(fluidInputs = it)) },
        FluidStack("", 1u)
    ) { value, updateValue ->
        MutableFluidRow(value, dataset::getFluid, dataset.allFluids, updateValue)
    }

    MutableLabeledList(
        "Item Outputs",
        recipe.itemOutputs.toTypedArray(),
        { manager.updateEdit(id, recipe.copy(itemOutputs = it)) },
        ItemOutputStack("", 1u, 100u)
    ) { value, updateValue ->
        MutableItemOutputRow(value, dataset::getItem, dataset.allItems, updateValue)
    }

    MutableLabeledList(
        "Fluid Outputs",
        recipe.fluidOutputs.toTypedArray(),
        { manager.updateEdit(id, recipe.copy(fluidOutputs = it)) },
        FluidStack("", 1u)
    ) { value, updateValue ->
        MutableFluidRow(value, dataset::getFluid, dataset.allFluids, updateValue)
    }
}