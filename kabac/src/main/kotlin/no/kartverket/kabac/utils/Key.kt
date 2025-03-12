package no.kartverket.kabac.utils

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.kartverket.kabac.AttributeValue

@Serializable(with = KeySerializer::class)
class Key<TYPE>(
    val name: String,
) {
    init {
        if (name.isEmpty()) {
            throw IllegalStateException("Key name cannot be empty")
        }
    }

    fun withValue(value: TYPE) = AttributeValue(this, value)

    override fun toString(): String = "Key($name)"

    override fun hashCode(): Int = name.hashCode()

    override fun equals(other: Any?): Boolean = when {
        other is Key<*> -> name == other.name
        name == other -> true
        else -> false
    }

    companion object {
        operator fun <T> invoke(any: Any): Key<T> {
            val qName = any::class.qualifiedName
                ?.removeSuffix(".Companion")
                ?.takeLastWhile { it != '.' }
                ?: any::class.java.simpleName
            return Key(qName)
        }
    }
}

class KeySerializer : KSerializer<Key<*>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Key<*> {
        return Key<Any>(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Key<*>) {
        encoder.encodeString(value.name)
    }
}