package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import state.AppState
import state.DataCategory
import state.WorkSet

@Composable
fun Sidebar(state: AppState) {
    Column(
        modifier = Modifier.fillMaxHeight().background(Color.White).padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EnumTabs(state.setState.selectedSet, WorkSet.entries)
        Divider(Modifier.width(40.dp))
        EnumTabs(state.setState.selectedCategory, DataCategory.entries)
    }
}