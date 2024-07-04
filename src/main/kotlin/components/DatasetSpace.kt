package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import state.*

@Composable
fun RowScope.DatasetSpace(data: DataManager, state: AppState) = Column(
    Modifier
        .fillMaxSize()
        .weight(1f)
        .background(Color(0xf0, 0xf0, 0xf0))
        .padding(2.dp)
) {
    ToolBar(data, state)
    LazyVerticalGrid(
        columns = GridCells.Adaptive(240.dp),
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val category = state.setState.selectedCategory.value
        val searchString = state.setState.searchString.value

        val itemList = data
            .list(state.setState.selectedSet.value, category)
            .filter { searchString in it.latest.trueName || searchString in it.latest.id }
            .run { state.setState.order.value.sort(this) { it.latest.id } }
            .toList()

        val categoryEquals = category == state.active.category.value

        items(items = itemList) {
            DataButton(it, categoryEquals, state.active)
        }
    }
}

@Composable
private fun ToolBar(manager: DataManager, state: AppState) = Row(
    modifier = Modifier.fillMaxWidth().padding(2.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    SearchBar(state.setState)
    OrderButton(state.setState.order)
    AddButton(state, manager)
}

@Composable
private fun AddButton(state: AppState, manager: DataManager) {
    TextButton(
        onClick = {
            val item = manager.primary.add(state.setState.selectedCategory.value)
            state.active.id.value = item.old.id
            state.active.category.value = item.old.category
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(40.dp)
    ) {
        Icon(Icons.Default.Add, "Add")
    }
}

@Composable
private fun OrderButton(state: MutableState<Order>) {
    TextButton(
        onClick = {
            state.value = when (state.value) {
                Order.Ascending -> Order.Descending
                Order.Descending -> Order.Ascending
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(40.dp)
    ) {
        when (state.value) {
            Order.Ascending -> Icon(Icons.Default.KeyboardArrowUp, "Ascending")
            Order.Descending -> Icon(Icons.Default.KeyboardArrowDown, "Descending")
        }
    }
}

@Composable
private fun RowScope.SearchBar(state: SetState) {
    val value = remember { mutableStateOf(TextFieldValue("")) }
    ImprovedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = Modifier.weight(0.8f),
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") }
    ) {
        state.searchString.value = value.value.text
    }
}

@Composable
private fun DataButton(state: DataItemState<*>, categoryEquals: Boolean, activeState: ActiveState) {
    val selected = categoryEquals && state.old.id == activeState.id.value
    Button(
        onClick = {
            if (selected) {
                activeState.id.value = null
            } else {
                activeState.id.value = state.old.id
                activeState.category.value = state.old.category
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = when {
                selected -> MaterialTheme.colors.primary
                state.current != null -> Color.Green
                else -> Color.White
            },
            contentColor = if (selected) MaterialTheme.colors.onPrimary else Color.DarkGray,
        ),
        contentPadding = PaddingValues(10.dp),
    ) {
        val item = state.latest
        Box(Modifier.height(34.dp).fillMaxWidth()) {
            Text(
                text = item.id,
                color = if (selected) Color.LightGray else Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                text = item.trueName,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
    }
}