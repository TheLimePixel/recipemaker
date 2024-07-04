package serialization

import data.Item

object ItemSerializer : Serializer<Item> {
    override fun serialize(obj: Item): String = StringBuilder()
        .serializeQuoted(Keys.name, obj.name)
        .serialize(Keys.recipeType, obj.recipeType)
        .toString()

    override fun deserialize(input: InputMap, id: String): Item = Item(
        id = id,
        name = input.deserializeString(Keys.name),
        recipeType = input.deserializeString(Keys.recipeType),
    )
}