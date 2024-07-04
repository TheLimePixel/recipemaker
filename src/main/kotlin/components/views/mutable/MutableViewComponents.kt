package components.views.mutable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import components.ImprovedTextField
import components.views.ActionButton
import data.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import state.DataItemState
import state.DataManager

@Composable
fun MutableDataView(
    id: String,
    data: DataItem,
    isNew: Boolean,
    dataManager: DataManager,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        ActiveName(data) { dataManager.updateEdit(id, data.renamed(data.id, it)) }
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxHeight()
                .weight(1f)
        ) {
            MutableIdField(data.id) { dataManager.updateEdit(id, data.renamed(it, data.name)) }
            content()
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ActionButton(
                onClick = {
                    dataManager.completeEditing(id, data.category)
                },
            ) {
                Icon(Icons.Rounded.Check, "Finish")
            }
            ActionButton({
                dataManager.removeEditing(id, data.category)
                if (isNew) dataManager.remove(id, data.category)
            }) {
                Icon(Icons.Rounded.Close, "Cancel")
            }
        }
    }
}

@Composable
fun ActiveName(item: DataItem, update: (String) -> Unit) {
    val textFieldValue = remember { mutableStateOf(TextFieldValue(item.name)) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        ImprovedTextField(
            value = textFieldValue.value,
            onValueChange = { newValue ->
                textFieldValue.value = newValue
                update(newValue.text)
            },
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 6.dp),
            defaultText = item.trueName,
            color = MaterialTheme.colors.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        Box(
            Modifier
                .height(1.dp)
                .width(100.dp)
                .background(MaterialTheme.colors.primary)
        )
    }
}

@Composable
fun MutableSingleDigitField(key: String, oldValue: UInt, effect: (UInt) -> Unit) {
    val text = remember { mutableStateOf(oldValue.toString()) }
    Column {
        Text(key, fontSize = 12.sp)
        BasicTextField(
            text.value,
            onValueChange = { newText ->
                when (newText.length) {
                    0 -> text.value = newText
                    1 -> newText.toUIntOrNull()?.let {
                        text.value = newText
                        effect(it)
                    }

                    else -> Unit
                }
            },
            singleLine = true,
        )
    }
}

@Composable
fun MutableDurationField(value: UInt, effect: (UInt) -> Unit) {
    Text("Duration", fontSize = 12.sp)
    Row(verticalAlignment = Alignment.Bottom) {
        MutableAmountField(value, effect)
        Text(" ticks")
    }
}

@Composable
fun MutableIdField(value: String, effect: (String) -> Unit) {
    Text("ID", fontSize = 12.sp)
    val state = remember { mutableStateOf(TextFieldValue(value)) }
    ImprovedTextField(state.value, onValueChange = { newState ->
        if (newState.text.all { it in 'a'..'z' || it == '_' }) {
            state.value = newState
            effect(newState.text)
        }
    })
}

@Composable
fun MutableAmountField(value: UInt, effect: (UInt) -> Unit) {
    val text = remember { mutableStateOf(TextFieldValue(value.toString())) }
    ImprovedTextField(
        value = text.value,
        onValueChange = { newValue ->
            val newText = newValue.text
            if (newText.isEmpty()) text.value = TextFieldValue("0", TextRange(1))
            else newText.toUIntOrNull()?.let {
                text.value = newValue.copy(text = it.toString())
                effect(it)
            }
        },
        modifier = Modifier.width(
            with(LocalDensity.current) {
                (9 * text.value.text.length.coerceAtLeast(1)).sp.toDp()
            }
        )
    )
}

@Composable
fun <T : DataItem> MutableDataField(
    key: String,
    current: T?,
    values: Sequence<DataItemState<T>>,
    effect: (String) -> Unit
) = Column {
    Text(key, fontSize = 12.sp)
    MutableDataDropdown(current, values, effect)
}

@Composable
fun <T : DataItem> MutableDataDropdown(
    currentItem: T?,
    valueSequence: Sequence<DataItemState<T>>,
    effect: (String) -> Unit
) {
    val allValues = valueSequence.map { it.old }.toList()
    val text = remember {
        mutableStateOf(TextFieldValue(currentItem?.trueName ?: ""))
    }
    val editing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val values = remember {
        mutableStateOf(allValues.filter { it.trueName.contains(text.value.text) })
    }
    val focusManager = LocalFocusManager.current

    Column {
        ImprovedTextField(
            text.value,
            onValueChange = { newText ->
                text.value = newText
                editing.value = newText.text.isNotBlank()
                scope.launch {
                    val newValues = allValues.filter { it.trueName.contains(newText.text) }
                    if (!coroutineContext.isActive) return@launch
                    values.value = newValues
                    when (newValues.size) {
                        0 -> effect("")
                        1 -> newValues.first().let { value ->
                            if (value.trueName == newText.text) {
                                editing.value = false
                                effect(value.id)
                            }
                        }
                    }
                }
            },
        )
        DropdownMenu(
            expanded = editing.value,
            onDismissRequest = { editing.value = false },
            properties = PopupProperties(focusable = false),
        ) {
            values.value.forEach {
                val name = it.trueName
                DropdownMenuItem({
                    text.value = TextFieldValue(name, TextRange(name.length))
                    editing.value = false
                    focusManager.clearFocus()
                    effect(it.id)
                }) {
                    Text("$name (${it.id})")
                }
            }
        }
    }
}

@Composable
fun <T : Any> MutableLabeledList(
    key: String,
    items: Array<out T>,
    updateList: (List<T>) -> Unit,
    default: T,
    component: @Composable (T, (T?) -> Unit) -> Unit,
) {
    val newList = remember { mutableStateListOf(*items) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(key, fontSize = 12.sp)
            TextButton(
                onClick = {
                    newList.add(default)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(with(LocalDensity.current) { 14.sp.toDp() })
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        }
        newList.forEachIndexed { index, value ->
            component(value) {
                if (it == null) newList.removeAt(index)
                else newList[index] = it
                updateList(newList.toList())
            }
        }
    }
}

@Composable
private fun <T> RemoveButton(fn: (T?) -> Unit) {
    TextButton(
        onClick = { fn(null) },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(with(LocalDensity.current) { 14.sp.toDp() })
    ) {
        Icon(Icons.Default.Remove, "Remove")
    }
}

@Composable
fun MutableItemInputRow(
    stack: ItemInputStack,
    getter: (String) -> DataItemState<Item>?,
    allItems: Sequence<DataItemState<Item>>,
    updateStack: (ItemInputStack?) -> Unit
) =
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MutableAmountField(stack.amount) { updateStack(stack.copy(amount = it)) }
        Text("x ")
        MutableDataDropdown(getter(stack.itemId)?.old, allItems) {
            updateStack(stack.copy(itemId = it))
        }
        Spacer(Modifier.weight(1f))
        RemoveButton(updateStack)
    }

@Composable
fun MutableItemOutputRow(
    stack: ItemOutputStack,
    getter: (String) -> DataItemState<Item>?,
    allItems: Sequence<DataItemState<Item>>,
    updateStack: (ItemOutputStack?) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MutableAmountField(stack.amount) { updateStack(stack.copy(amount = it)) }
        Text("x ")
        MutableDataDropdown(getter(stack.itemId)?.old, allItems) {
            updateStack(stack.copy(itemId = it))
        }
        MutableAmountField(stack.chance) { updateStack(stack.copy(chance = it)) }
        Text("%")
        Spacer(Modifier.weight(1f))
        RemoveButton(updateStack)
    }
}

@Composable
fun MutableFluidRow(
    stack: FluidStack,
    getter: (String) -> DataItemState<Fluid>?,
    allFluids: Sequence<DataItemState<Fluid>>,
    updateStack: (FluidStack?) -> Unit
) =
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MutableAmountField(stack.amount) { updateStack(stack.copy(amount = it)) }
        Text("x ")
        MutableDataDropdown(getter(stack.fluidId)?.old, allFluids) {
            updateStack(stack.copy(fluidId = it))
        }
        Spacer(Modifier.weight(1f))
        RemoveButton(updateStack)
    }