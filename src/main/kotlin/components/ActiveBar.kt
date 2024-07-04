package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.views.immutable.ImmutableFluidView
import components.views.immutable.ImmutableItemView
import components.views.immutable.ImmutableRecipeTypeView
import components.views.immutable.ImmutableRecipeView
import components.views.mutable.MutableFluidView
import components.views.mutable.MutableItemView
import components.views.mutable.MutableRecipeTypeView
import components.views.mutable.MutableRecipeView
import state.AppState
import state.DataCategory
import state.DataManager

@Composable
fun ActiveBar(data: DataManager, state: AppState) {
    Column(Modifier.width(360.dp)) {
        val active = state.active
        val (value, valueSetter) = active.id
        value?.let {
            val dataset = data.primary

            when (active.category.value) {
                DataCategory.Items -> dataset::getItem.getOrNullify(value, valueSetter) { state ->
                    state.current?.let { MutableItemView(state.old.id, it, state.isNew, data) }
                        ?: ImmutableItemView(state.old, active.deleting, data)
                }

                DataCategory.Fluids -> dataset::getFluid.getOrNullify(value, valueSetter) { state ->
                    state.current?.let { MutableFluidView(state.old.id, it, state.isNew, data) }
                        ?: ImmutableFluidView(state.old, active.deleting, data)
                }

                DataCategory.Recipes -> dataset::getRecipe.getOrNullify(value, valueSetter) { state ->
                    state.current?.let { MutableRecipeView(state.old.id, it, state.isNew, data) }
                        ?: ImmutableRecipeView(state.old, active.deleting, data)
                }

                DataCategory.RecipeTypes -> dataset::getRecipeType.getOrNullify(value, valueSetter) { state ->
                    state.current?.let { MutableRecipeTypeView(state.old.id, it, state.isNew, data) }
                        ?: ImmutableRecipeTypeView(state.old, active.deleting, data)
                }
            }
        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nothing selected", color = Color.LightGray)
            }
        }
    }
}

private inline fun <T> ((String) -> T?).getOrNullify(
    value: String,
    valueSetter: (String?) -> Unit,
    then: (T) -> Unit
) {
    this(value)?.let {
        then(it)
    } ?: valueSetter(null)
}