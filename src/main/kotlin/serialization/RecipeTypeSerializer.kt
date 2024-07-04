package serialization

import data.RecipeType

object RecipeTypeSerializer : Serializer<RecipeType> {
    override fun serialize(obj: RecipeType): String = StringBuilder()
        .serializeQuoted(Keys.name, obj.name)
        .serialize(Keys.maxItemInputs, obj.maxItemInputs, default = 0u)
        .serialize(Keys.maxFluidInputs, obj.maxFluidInputs, default = 0u)
        .serialize(Keys.maxItemOutputs, obj.maxItemOutputs, default = 0u)
        .serialize(Keys.maxFluidOutputs, obj.maxFluidOutputs, default = 0u)
        .serialize(Keys.inheritsFrom, obj.inheritsFrom)
        .toString()

    override fun deserialize(input: InputMap, id: String): RecipeType = RecipeType(
        id = id,
        name = input.deserializeString(Keys.name),
        maxItemInputs = input.deserializeNum(Keys.maxItemInputs, default = 0u),
        maxFluidInputs = input.deserializeNum(Keys.maxFluidInputs, default = 0u),
        maxItemOutputs = input.deserializeNum(Keys.maxItemOutputs, default = 0u),
        maxFluidOutputs = input.deserializeNum(Keys.maxFluidOutputs, default = 0u),
        inheritsFrom = input.deserializeString(Keys.inheritsFrom),
    )
}