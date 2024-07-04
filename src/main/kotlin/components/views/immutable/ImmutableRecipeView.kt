package components.views.immutable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import data.Recipe
import state.DataManager

@Composable
fun ImmutableRecipeView(
    recipe: Recipe,
    deleting: MutableState<Boolean>,
    data: DataManager,
) = ImmutableDataView(recipe, deleting, data) {
    val dataset = data.primary
    ImmutableLabeledItemName("Recipe Type", recipe.recipeType, dataset::getRecipeType)
    ImmutableDurationField(recipe.duration)
    ImmutableLabeledList("Item Inputs", recipe.itemInputs) { ImmutableItemInputRow(it, dataset) }
    ImmutableLabeledList("Fluid Inputs", recipe.fluidInputs) { ImmutableFluidRow(it, dataset) }
    ImmutableLabeledList("Item Outputs", recipe.itemOutputs) { ImmutableItemOutputRow(it, dataset) }
    ImmutableLabeledList("Fluid Outputs", recipe.fluidOutputs) { ImmutableFluidRow(it, dataset) }
}