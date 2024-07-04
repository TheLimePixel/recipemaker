import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import components.ProjectScreen
import components.ProjectSelection

fun main() = application {
    var path by remember { mutableStateOf("") }
    val position = WindowPosition(Alignment.Center)

    MaterialTheme {
        if (path.isBlank()) {
            val state = rememberWindowState(position = position, width = 500.dp, height = 300.dp)
            Window(
                onCloseRequest = ::exitApplication,
                title = "Pixel's Recipe Maker - Project Selection",
                state = state
            ) {
                ProjectSelection { path = it }
            }
        } else {
            val state = rememberWindowState(
                placement = WindowPlacement.Maximized,
                position = position,
                isMinimized = false,
            )
            Window(
                onCloseRequest = ::exitApplication, title = "Pixel's Recipe Maker",
                state = state
            ) {
                ProjectScreen(path)
            }
        }
    }
}
