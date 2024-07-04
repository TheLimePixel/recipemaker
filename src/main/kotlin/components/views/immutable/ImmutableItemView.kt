package components.views.immutable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import data.Item
import state.DataManager

@Composable
fun ImmutableItemView(
    item: Item,
    deleting: MutableState<Boolean>,
    data: DataManager,
) = ImmutableDataView(item, deleting, data) {
    ImmutableLabeledItemName("Recipe Type", item.recipeType, data.primary::getRecipeType)
}