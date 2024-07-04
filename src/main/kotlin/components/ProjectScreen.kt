package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import state.AppState
import state.DataManager

@Composable
fun ProjectScreen(path: String) {
    val loaded = remember { mutableStateOf(false) }
    val data = remember { DataManager(path) }
    val state = remember { AppState() }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(path, loaded)
        Divider(color = Color.LightGray)

        if (loaded.value) ProjectPage(data, state)
        else ReloadingPage(data, loaded)
    }
}

@Composable
private fun TopBar(path: String, loaded: MutableState<Boolean>) {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Project: $path",
            fontSize = 12.sp
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { loaded.value = false },
            modifier = Modifier.size(26.dp),
            contentPadding = PaddingValues(2.dp),
            enabled = loaded.value
        ) {
            Icon(Icons.Rounded.Refresh, "Reload")
        }
    }
}