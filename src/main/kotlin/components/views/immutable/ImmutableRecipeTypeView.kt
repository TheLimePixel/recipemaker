package components.views.immutable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import data.RecipeType
import state.DataManager

@Composable
fun ImmutableRecipeTypeView(
    recipeType: RecipeType,
    deleting: MutableState<Boolean>,
    data: DataManager,
) = ImmutableDataView(recipeType, deleting, data) {
    ImmutableLabeledTextField("Item Inputs", recipeType.maxItemInputs)
    ImmutableLabeledTextField("Fluid Inputs", recipeType.maxFluidInputs)
    ImmutableLabeledTextField("Item Outputs", recipeType.maxItemOutputs)
    ImmutableLabeledTextField("Fluid Outputs", recipeType.maxFluidOutputs)
    ImmutableLabeledItemName("Inherits From", recipeType.inheritsFrom, data.primary::getRecipeType)
}