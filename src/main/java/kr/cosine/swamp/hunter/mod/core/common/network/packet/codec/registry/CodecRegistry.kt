package kr.cosine.swamp.hunter.mod.core.common.network.packet.codec.registry

import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet
import kr.cosine.swamp.hunter.mod.core.common.network.packet.codec.Codec
import kotlin.reflect.KClass

object CodecRegistry {
    private val codecMap = mutableMapOf<String, Codec<out Packet>>()

    fun set(className: String, codec: Codec<out Packet>) {
        codecMap[className] = codec
    }

    fun set(clazz: KClass<*>, codec: Codec<out Packet>) {
        set(clazz.qualifiedName!!, codec)
    }

    fun find(className: String): Codec<out Packet>? {
        return codecMap[className]
    }

    fun find(clazz: KClass<*>): Codec<out Packet>? {
        return find(clazz.qualifiedName!!)
    }
}