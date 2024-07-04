package state

import androidx.compose.runtime.Stable
import data.*
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import serialization.*
import state.DataCategory.*
import java.io.File
import kotlin.io.path.Path

@Stable
class DataManager(private val path: String) {
    val primary = ConcreteDataset()
    val pinned = FilteringDataset(primary)
    val errors = FilteringDataset(primary)
    val editing = EditingDataset(primary)

    @Suppress("UNCHECKED_CAST")
    fun load() {
        primary.clear()
        pinned.clear()
        errors.clear()

        val load = Load(LoadSettings.builder().build())

        fun <T : DataItem> loadType(
            type: String,
            serializer: Serializer<T>,
            addFn: (String, DataItemState<T>) -> Unit
        ) = File(path, type)
            .listFiles()
            ?.asSequence()
            ?.filter { it.canRead() && it.extension == "yml" }
            ?.forEach { file ->
                val id = file.nameWithoutExtension
                val parsed = load.loadAllFromString(file.readText()).iterator()
                val input: InputMap = if (parsed.hasNext()) {
                    parsed.next() as? Map<String, Any> ?: emptyMap()
                } else emptyMap()
                addFn(id, DataItemState(false, serializer.deserialize(input, id), null))
            }

        loadType(Items.dirName, ItemSerializer, primary::setItem)
        loadType(Fluids.dirName, FluidSerializer, primary::setFluid)
        loadType(Recipes.dirName, RecipeSerializer, primary::setRecipe)
        loadType(RecipeTypes.dirName, RecipeTypeSerializer, primary::setRecipeType)
    }

    fun list(set: WorkSet, type: DataCategory): Sequence<DataItemState<*>> {
        val dataset = when (set) {
            WorkSet.Primary -> primary
            WorkSet.Pinned -> pinned
            WorkSet.Editing -> editing
            WorkSet.Errors -> errors
        }
        return when (type) {
            Items -> dataset.allItems
            Fluids -> dataset.allFluids
            Recipes -> dataset.allRecipes
            RecipeTypes -> dataset.allRecipeTypes
        }
    }

    fun remove(id: String, category: DataCategory) {
        primary.remove(id, category)
        Path(path, category.dirName, "$id.yml").toFile().delete()
    }

    fun removeEditing(id: String, category: DataCategory) {
        when (category) {
            Items -> update(id, null, primary::getItem, primary::setItem)
            Fluids -> update(id, null, primary::getFluid, primary::setFluid)
            Recipes -> update(id, null, primary::getRecipe, primary::setRecipe)
            RecipeTypes -> update(id, null, primary::getRecipeType, primary::setRecipeType)
        }
    }

    fun completeEditing(id: String, category: DataCategory) {
        fun <T : DataItem> finish(
            serializer: Serializer<T>,
            get: (String) -> DataItemState<T>?,
            set: (String, DataItemState<T>) -> Unit,
        ) {
            val item = get(id)?.current ?: return
            Path(path, item.category.dirName).toFile().mkdir()
            Path(path, item.category.dirName, item.id + ".yml").toFile().writeText(serializer.serialize(item))
            set(id, DataItemState(false, item, null))
        }

        when (category) {
            Items -> finish(ItemSerializer, primary::getItem, primary::setItem)
            Fluids -> finish(FluidSerializer, primary::getFluid, primary::setFluid)
            Recipes -> finish(RecipeSerializer, primary::getRecipe, primary::setRecipe)
            RecipeTypes -> finish(RecipeTypeSerializer, primary::getRecipeType, primary::setRecipeType)
        }
    }

    fun updateEdit(id: String, item: DataItem) {
        when (item) {
            is Item -> update(id, item, primary::getItem, primary::setItem)
            is Fluid -> update(id, item, primary::getFluid, primary::setFluid)
            is Recipe -> update(id, item, primary::getRecipe, primary::setRecipe)
            is RecipeType -> update(id, item, primary::getRecipeType, primary::setRecipeType)
        }
    }

    private fun <T: DataItem> update(
        id: String,
        item: T?,
        get: (String) -> DataItemState<T>?,
        set: (String, DataItemState<T>) -> Unit
    ) {
        val oldState = get(id) ?: return
        set(id, oldState.copy(current = item))
    }
}