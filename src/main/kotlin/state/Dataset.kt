package state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import data.*

@Stable
sealed interface Dataset {
    val allItems: Sequence<DataItemState<Item>>
    val allFluids: Sequence<DataItemState<Fluid>>
    val allRecipes: Sequence<DataItemState<Recipe>>
    val allRecipeTypes: Sequence<DataItemState<RecipeType>>

    fun clear()

    fun remove(id: String, category: DataCategory)

    fun contains(category: DataCategory, id: String): Boolean

    fun getItem(id: String): DataItemState<Item>?

    fun getFluid(id: String): DataItemState<Fluid>?

    fun getRecipe(id: String): DataItemState<Recipe>?

    fun getRecipeType(id: String): DataItemState<RecipeType>?
}

@Stable
class FilteringDataset(private val delegate: Dataset) : Dataset {
    private val selectedItems: MutableMap<String, Unit> = mutableStateMapOf()
    private val selectedFluids: MutableMap<String, Unit> = mutableStateMapOf()
    private val selectedRecipes: MutableMap<String, Unit> = mutableStateMapOf()
    private val selectedRecipeTypes: MutableMap<String, Unit> = mutableStateMapOf()

    override val allItems: Sequence<DataItemState<Item>>
        get() = selectedItems.keys.asSequence().map { delegate.getItem(it)!! }

    override val allFluids: Sequence<DataItemState<Fluid>>
        get() = selectedFluids.keys.asSequence().map { delegate.getFluid(it)!! }

    override val allRecipes: Sequence<DataItemState<Recipe>>
        get() = selectedRecipes.keys.asSequence().map { delegate.getRecipe(it)!! }

    override val allRecipeTypes: Sequence<DataItemState<RecipeType>>
        get() = selectedRecipeTypes.keys.asSequence().map { delegate.getRecipeType(it)!! }

    override fun clear() {
        selectedItems.clear()
        selectedFluids.clear()
        selectedRecipes.clear()
        selectedRecipeTypes.clear()
    }

    private fun mapForCategory(category: DataCategory) = when (category) {
        DataCategory.Items -> selectedItems
        DataCategory.Fluids -> selectedFluids
        DataCategory.Recipes -> selectedRecipes
        DataCategory.RecipeTypes -> selectedRecipeTypes
    }

    fun add(category: DataCategory, id: String) {
        mapForCategory(category)[id] = Unit
    }

    override fun remove(id: String, category: DataCategory) {
        mapForCategory(category).remove(id)
    }

    override fun contains(category: DataCategory, id: String): Boolean =
        mapForCategory(category).contains(id)

    override fun getItem(id: String): DataItemState<Item>? =
        delegate.getItem(id)

    override fun getFluid(id: String): DataItemState<Fluid>? =
        delegate.getFluid(id)

    override fun getRecipe(id: String): DataItemState<Recipe>? =
        delegate.getRecipe(id)

    override fun getRecipeType(id: String): DataItemState<RecipeType>? =
        delegate.getRecipeType(id)
}

@Stable
class ConcreteDataset : Dataset {
    private val items: MutableMap<String, DataItemState<Item>> = mutableStateMapOf()
    private val fluids: MutableMap<String, DataItemState<Fluid>> = mutableStateMapOf()
    private val recipes: MutableMap<String, DataItemState<Recipe>> = mutableStateMapOf()
    private val recipeTypes: MutableMap<String, DataItemState<RecipeType>> = mutableStateMapOf()

    override val allItems: Sequence<DataItemState<Item>>
        get() = items.values.asSequence()

    override val allFluids: Sequence<DataItemState<Fluid>>
        get() = fluids.values.asSequence()

    override val allRecipes: Sequence<DataItemState<Recipe>>
        get() = recipes.values.asSequence()

    override val allRecipeTypes: Sequence<DataItemState<RecipeType>>
        get() = recipeTypes.values.asSequence()

    override fun clear() {
        items.clear()
        fluids.clear()
        recipes.clear()
        recipeTypes.clear()
    }

    private fun mapForCategory(category: DataCategory) = when (category) {
        DataCategory.Items -> items
        DataCategory.Fluids -> fluids
        DataCategory.Recipes -> recipes
        DataCategory.RecipeTypes -> recipeTypes
    }

    override fun remove(id: String, category: DataCategory) {
        mapForCategory(category).remove(id)
    }

    override fun contains(category: DataCategory, id: String): Boolean =
        mapForCategory(category).contains(id)

    fun add(category: DataCategory): DataItemState<*> {
        fun <T : DataItem> genIdAndAdd(
            map: MutableMap<String, DataItemState<T>>,
            ctor: (id: String) -> T
        ): DataItemState<T> {
            var id = category.defaultId
            var counter = 0

            while (id in map) {
                counter += 1
                id = "${category.defaultId}_$counter"
            }

            val state = DataItemState(true, ctor(id), ctor(category.defaultId))
            map[id] = state
            return state
        }

        return when (category) {
            DataCategory.Items -> genIdAndAdd(items, ::Item)
            DataCategory.Fluids -> genIdAndAdd(fluids, ::Fluid)
            DataCategory.Recipes -> genIdAndAdd(recipes, ::Recipe)
            DataCategory.RecipeTypes -> genIdAndAdd(recipeTypes, ::RecipeType)
        }
    }

    override fun getItem(id: String): DataItemState<Item>? =
        items[id]

    override fun getFluid(id: String): DataItemState<Fluid>? =
        fluids[id]

    override fun getRecipe(id: String): DataItemState<Recipe>? =
        recipes[id]

    override fun getRecipeType(id: String): DataItemState<RecipeType>? =
        recipeTypes[id]

    fun setItem(id: String, item: DataItemState<Item>) {
        items[id] = item
    }

    fun setFluid(id: String, item: DataItemState<Fluid>) {
        fluids[id] = item
    }

    fun setRecipe(id: String, item: DataItemState<Recipe>) {
        recipes[id] = item
    }

    fun setRecipeType(id: String, item: DataItemState<RecipeType>) {
        recipeTypes[id] = item
    }
}

@Stable
class EditingDataset(private val delegate: Dataset): Dataset by delegate {
    override val allItems: Sequence<DataItemState<Item>>
        get() = delegate.allItems.filter { it.current != null }

    override val allFluids: Sequence<DataItemState<Fluid>>
        get() = delegate.allFluids.filter { it.current != null }

    override val allRecipes: Sequence<DataItemState<Recipe>>
        get() = delegate.allRecipes.filter { it.current != null }

    override val allRecipeTypes: Sequence<DataItemState<RecipeType>>
        get() = delegate.allRecipeTypes.filter { it.current != null }
}