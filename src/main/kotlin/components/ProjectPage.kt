package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import state.AppState
import state.DataManager

@Composable
fun ProjectPage(data: DataManager, state: AppState) {
    Row(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        Sidebar(state)
        VerticalDivider()
        DatasetSpace(data, state)
        VerticalDivider()
        ActiveBar(data, state)
    }
}