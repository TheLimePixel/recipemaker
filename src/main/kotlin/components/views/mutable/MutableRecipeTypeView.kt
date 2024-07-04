package components.views.mutable

import androidx.compose.runtime.Composable
import data.RecipeType
import state.DataManager

@Composable
fun MutableRecipeTypeView(
    id: String,
    recipeType: RecipeType,
    isNew: Boolean,
    manager: DataManager,
) = MutableDataView(id, recipeType, isNew, manager) {
    MutableSingleDigitField("Item Inputs", recipeType.maxItemInputs) {
        manager.updateEdit(id, recipeType.copy(maxItemInputs = it))
    }
    MutableSingleDigitField("Fluid Inputs", recipeType.maxFluidInputs) {
        manager.updateEdit(id, recipeType.copy(maxFluidInputs = it))
    }
    MutableSingleDigitField("Item Outputs", recipeType.maxItemOutputs) {
        manager.updateEdit(id, recipeType.copy(maxItemOutputs = it))
    }
    MutableSingleDigitField("Fluid Outputs", recipeType.maxFluidOutputs) {
        manager.updateEdit(id, recipeType.copy(maxFluidOutputs = it))
    }
    val dataset = manager.primary
    MutableDataField("Inherits From", dataset.getRecipeType(recipeType.inheritsFrom)?.old, dataset.allRecipeTypes) {
        manager.updateEdit(id, recipeType.copy(inheritsFrom = it))
    }
}