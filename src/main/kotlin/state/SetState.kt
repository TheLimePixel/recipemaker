package state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import state.WorkSet.*

@Stable
class SetState {
    val selectedSet = mutableStateOf(Primary)
    val selectedCategory = mutableStateOf(DataCategory.Items)
    val searchString = mutableStateOf("")
    val order = mutableStateOf(Order.Ascending)
    val sorting = mutableStateOf(Sorting.Name)
}

enum class Sorting {
    Name,
    Uses,
    Recipes;
}

enum class Order {
    Ascending {
        override fun <T, S : Comparable<S>> sort(sequence: Sequence<T>, accessor: (T) -> S): Sequence<T> =
            sequence.sortedBy(accessor)
    },
    Descending {
        override fun <T, S : Comparable<S>> sort(sequence: Sequence<T>, accessor: (T) -> S): Sequence<T> =
            sequence.sortedByDescending(accessor)
    };

    abstract fun <T, S : Comparable<S>> sort(sequence: Sequence<T>, accessor: (T) -> S): Sequence<T>
}