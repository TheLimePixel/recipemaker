package components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import java.nio.file.Files
import java.nio.file.InvalidPathException
import kotlin.io.path.Path

@Composable
fun ProjectSelection(onSelection: (String) -> Unit) {
    var pathField by remember { mutableStateOf(TextFieldValue("")) }
    var isSelecting by remember { mutableStateOf(false) }

    val path = pathField.text
    fun isValidPath(): Boolean = try {
        path.isNotBlank() && Files.isDirectory(Path(path)) && Files.isReadable(Path(path))
    } catch (e: InvalidPathException) {
        false
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.size(400.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment
                .CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ImprovedTextField(
                    pathField,
                    onValueChange = { pathField = it },
                    modifier = Modifier.height(50.dp).width(340.dp).border(1.dp, Color.LightGray),
                    contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 1.dp, bottom = 1.dp),
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { isSelecting = true },
                    modifier = Modifier.size(50.dp),
                    contentPadding = PaddingValues(1.dp)
                ) {
                    Icon(Icons.Rounded.Search, contentDescription = "Search")
                }
            }
            Button(onClick = { onSelection(path) }, Modifier.fillMaxWidth(), enabled = isValidPath()) {
                Text("Select")
            }
        }
    }

    DirectoryPicker(
        isSelecting,
        onFileSelected = { file ->
            file?.let { pathField = TextFieldValue(file, selection = TextRange(file.length)) }
            isSelecting = false
        },
        title = "Select project"
    )
}