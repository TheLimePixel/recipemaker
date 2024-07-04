package serialization

interface Serializer<T> {
    fun serialize(obj: T): String
    fun deserialize(input: InputMap, id: String): T
}

typealias InputMap = Map<String, Any?>

fun StringBuilder.serialize(key: String, value: String?, prefix: String = ""): StringBuilder = value?.let {
    append(prefix).append("$key: $value\n")
} ?: this

fun StringBuilder.serializeQuoted(key: String, value: String?): StringBuilder = value?.let {
    append("$key: \"$value\"\n")
} ?: this

fun StringBuilder.serialize(key: String, value: UInt, prefix: String = "", default: UInt): StringBuilder =
    if (value == default) this
    else append(prefix).append("$key: $value\n")

inline fun <T> StringBuilder.serialize(
    key: String,
    value: List<T>?,
    itemSerializer: (T, StringBuilder) -> StringBuilder
): StringBuilder = this.also {
    value?.let { value ->
        append("$key:\n")
        value.forEach { itemSerializer(it, this) }
    }
}

fun InputMap.deserializeString(key: String) = get(key)?.toString() ?: ""

fun InputMap.deserializeNum(key: String, default: UInt) = (get(key) as? Int)?.toUInt() ?: default
@Suppress("UNCHECKED_CAST")
inline fun <T: Any> InputMap.deserializeList(key: String, serializationFunction: (InputMap) -> T): List<T> =
    (get(key) as? List<*>)
        ?.mapNotNull { if (it !is Map<*, *>) null else serializationFunction(it as Map<String, Any>) }
        ?: emptyList()