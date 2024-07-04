package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import state.Displayable

@Composable
fun <T : Displayable> EnumTabs(selected: MutableState<T>, entries: List<T>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(entries, key = { it }) {
            Button(
                modifier = Modifier.size(40.dp),
                enabled = selected.value != it,
                onClick = { selected.value = it },
                elevation = ButtonDefaults.elevation(1.dp),
                contentPadding = PaddingValues(2.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContentColor = MaterialTheme.colors.onPrimary,
                    disabledBackgroundColor = MaterialTheme.colors.primary,
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black,
                )
            ) {
                Icon(it.icon, it.displayName)
            }
        }
    }
}