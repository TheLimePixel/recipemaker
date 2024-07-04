package components.views.mutable

import androidx.compose.runtime.Composable
import data.Item
import state.DataManager

@Composable
fun MutableItemView(
    id: String,
    item: Item,
    isNew: Boolean,
    manager: DataManager,
) = MutableDataView(id, item, isNew, manager) {
    val dataset = manager.primary

    MutableDataField("Recipe Type", dataset.getRecipeType(item.recipeType)?.old, dataset.allRecipeTypes) { type ->
        manager.updateEdit(id, item.copy(recipeType = type))
    }
}