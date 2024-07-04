package components.views.immutable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import components.views.ActionButton
import data.DataItem
import data.FluidStack
import data.ItemInputStack
import data.ItemOutputStack
import state.ConcreteDataset
import state.DataItemState
import state.DataManager

@Composable
fun ImmutableDataView(
    data: DataItem,
    deleting: MutableState<Boolean>,
    dataManager: DataManager,
    content: @Composable ColumnScope.() -> Unit
) {
    val id = data.id
    val name = data.trueName
    val category = data.category

    if (deleting.value) {
        DeleteDialog(name, { deleting.value = false }) { dataManager.remove(id, category) }
    }

    Column {
        ActiveName(name)
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxHeight()
                .weight(1f)
        ) {
            ImmutableLabeledTextField("ID", id)
            content()
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ActionButton(onClick = { dataManager.updateEdit(id, data) }) {
                Icon(Icons.Rounded.Edit, "Edit")
            }
            val pinned = dataManager.pinned.contains(category, id)
            ActionButton({
                if (pinned) dataManager.pinned.remove(id, category)
                else dataManager.pinned.add(category, id)
            }) {
                if (pinned)
                    Icon(Icons.Filled.PushPin, "Unpin")
                else
                    Icon(Icons.Outlined.PushPin, "Pin")
            }
            ActionButton({ deleting.value = true }) {
                Icon(Icons.Rounded.Delete, "Delete")
            }
        }
    }
}

@Composable
fun ActiveName(name: String) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 6.dp),
            text = name,
            color = MaterialTheme.colors.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        Box(Modifier
            .height(1.dp)
            .width(100.dp)
            .background(MaterialTheme.colors.primary)
        )
    }
}

@Composable
fun DeleteDialog(name: String, stopDeleting: () -> Unit, delete: () -> Unit) {
    Dialog(onDismissRequest = stopDeleting) {
        Column(
            modifier = Modifier.background(Color.White).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Delete $name?")
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Button(
                    onClick = {
                        delete()
                        stopDeleting()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
                Button(
                    onClick = stopDeleting,
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun ImmutableLabeledTextField(key: String, value: Any) {
    Column {
        Text(key, fontSize = 12.sp)
        Text(value.toString())
    }
}

@Composable
fun ImmutableDurationField(value: UInt) {
    Column {
        Text("Duration", fontSize = 12.sp)
        Text("$value ticks")
    }
}

@Composable
fun ImmutableLabeledItemName(key: String, id: String, getter: (String) -> DataItemState<DataItem>?) {
    if (id.isNotEmpty()) Column {
        Text(key, fontSize = 12.sp)
        ItemName(id, getter)
    }
}

@Composable
fun <T> ImmutableLabeledList(key: String, list: List<T>, component: @Composable (T) -> Unit) {
    if (list.isNotEmpty()) Column {
        Text(key, fontSize = 12.sp)
        list.forEach { component(it) }
    }
}

@Composable
fun ImmutableItemInputRow(stack: ItemInputStack, dataset: ConcreteDataset) {
    Row {
        Text("${stack.amount}x ")
        ItemName(stack.itemId, dataset::getItem)
    }
}

@Composable
fun ImmutableItemOutputRow(stack: ItemOutputStack, dataset: ConcreteDataset) {
    Row {
        Text("${stack.amount}x ")
        if (stack.chance != 100u) Text("${stack.chance / 100u}.${stack.chance % 100u}% ")
        ItemName(stack.itemId, dataset::getItem)
    }
}

@Composable
fun ImmutableFluidRow(stack: FluidStack, dataset: ConcreteDataset) {
    Row {
        Text("${stack.amount}x")
        ItemName(stack.fluidId, dataset::getFluid)
    }
}

@Composable
private fun ItemName(id: String, get: (id: String) -> DataItemState<DataItem>?) {
    get(id)?.let { state ->
        val item = state.old
        Text(item.trueName)
    } ?: Text(id, color = Color.Red)
}