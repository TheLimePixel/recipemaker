package state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

interface Displayable {
    val displayName: String
    val icon: ImageVector
}

@Immutable
enum class DataCategory(
    override val displayName: String,
    override val icon: ImageVector,
    val dirName: String,
    val defaultId: String,
) : Displayable {
    Items("Items", Icons.Default.Carpenter, "items", "new_item"),
    Fluids("Fluids", Icons.Default.WaterDrop, "fluids", "new_fluid"),
    Recipes("Recipes", Icons.AutoMirrored.Filled.Article, "recipes", "new_recipe"),
    RecipeTypes("Recipe Types", Icons.Default.Memory, "recipe_types", "new_recipe_type"),
}

@Immutable
enum class WorkSet(override val displayName: String, override val icon: ImageVector) : Displayable {
    Primary("Primary", Icons.Default.Home),
    Pinned("Pinned", Icons.Default.PushPin),
    Editing("Editing", Icons.Default.Edit),
    Errors("Errors", Icons.Default.Warning),
}