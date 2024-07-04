package serialization

import data.FluidStack
import data.ItemInputStack
import data.ItemOutputStack
import data.Recipe

private const val listStart = "  - "
private const val listContinuation = "    "

object RecipeSerializer : Serializer<Recipe> {
    override fun serialize(obj: Recipe): String = StringBuilder()
        .serializeQuoted(Keys.name, obj.name)
        .serialize(Keys.recipeType, obj.recipeType)
        .serialize(Keys.duration, obj.duration, default = 0u)
        .serialize(Keys.itemInputs, obj.itemInputs, ::serializeItemInputStack)
        .serialize(Keys.fluidInputs, obj.fluidInputs, ::serializeFluidStack)
        .serialize(Keys.itemOutputs, obj.itemOutputs, ::serializeItemOutputStack)
        .serialize(Keys.fluidOutputs, obj.fluidOutputs, ::serializeFluidStack)
        .toString()

    override fun deserialize(input: InputMap, id: String): Recipe = Recipe(
        id = id,
        name = input.deserializeString(Keys.name),
        recipeType = input.deserializeString(Keys.recipeType),
        duration = input.deserializeNum(Keys.duration, default = 0u),
        itemInputs = input.deserializeList(Keys.itemInputs, ::deserializeItemInputStack),
        fluidInputs = input.deserializeList(Keys.fluidInputs, ::deserializeFluidStack),
        itemOutputs = input.deserializeList(Keys.itemOutputs, ::deserializeItemOutputStack),
        fluidOutputs = input.deserializeList(Keys.fluidOutputs, ::deserializeFluidStack),
    )
}

private fun serializeFluidStack(stack: FluidStack, builder: StringBuilder): StringBuilder = builder
    .serialize(Keys.id, stack.fluidId, prefix = listStart)
    .serialize(Keys.amount, stack.amount, prefix = listContinuation, default = 1u)

private fun serializeItemInputStack(stack: ItemInputStack, builder: StringBuilder): StringBuilder = builder
    .serialize(Keys.id, stack.itemId, prefix = listStart)
    .serialize(Keys.amount, stack.amount, prefix = listContinuation, default = 1u)

private fun serializeItemOutputStack(stack: ItemOutputStack, builder: StringBuilder): StringBuilder = builder
    .serialize(Keys.id, stack.itemId, prefix = listStart)
    .serialize(Keys.amount, stack.amount, prefix = listContinuation, default = 1u)
    .serialize(Keys.chance, stack.chance, prefix = listContinuation, default = 100u)

private fun deserializeFluidStack(input: InputMap): FluidStack = FluidStack(
    fluidId = input.deserializeString(Keys.id),
    amount = input.deserializeNum(Keys.amount, default = 1u),
)

private fun deserializeItemInputStack(input: InputMap): ItemInputStack = ItemInputStack(
    itemId = input.deserializeString(Keys.id),
    amount = input.deserializeNum(Keys.amount, default = 1u),
)

private fun deserializeItemOutputStack(input: InputMap): ItemOutputStack = ItemOutputStack(
    itemId = input.deserializeString(Keys.id),
    amount = input.deserializeNum(Keys.amount, default = 1u),
    chance = input.deserializeNum(Keys.chance, default = 100u),
)