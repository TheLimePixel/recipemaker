package components.views.immutable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import data.Fluid
import state.DataManager

@Composable
fun ImmutableFluidView(
    fluid: Fluid,
    deleting: MutableState<Boolean>,
    data: DataManager,
) = ImmutableDataView(fluid, deleting, data) { }