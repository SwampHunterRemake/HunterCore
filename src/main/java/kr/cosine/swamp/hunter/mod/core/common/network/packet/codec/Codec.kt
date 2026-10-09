package kr.cosine.swamp.hunter.mod.core.common.network.packet.codec

import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

data class Codec<T : Packet>(
    val packetClass: KClass<T>,
    val codecClass: Class<*>
) {
    val packetClassPrimaryConstructor by lazy { packetClass.primaryConstructor!! }

    val codecClassConstructor by lazy { codecClass.getConstructor() }
}