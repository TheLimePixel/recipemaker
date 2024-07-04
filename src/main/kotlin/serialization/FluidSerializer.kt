package serialization

import data.Fluid

object FluidSerializer : Serializer<Fluid> {
    override fun serialize(obj: Fluid): String = StringBuilder()
        .serializeQuoted(Keys.name, obj.name)
        .toString()

    override fun deserialize(input: InputMap, id: String): Fluid = Fluid(
        id = id,
        name = input.deserializeString(Keys.name),
    )
}