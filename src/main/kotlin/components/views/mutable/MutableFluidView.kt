package components.views.mutable

import androidx.compose.runtime.Composable
import data.Fluid
import state.DataManager

@Composable
fun MutableFluidView(
    id: String,
    fluid: Fluid,
    isNew: Boolean,
    manager: DataManager,
) = MutableDataView(id, fluid, isNew, manager) {
}