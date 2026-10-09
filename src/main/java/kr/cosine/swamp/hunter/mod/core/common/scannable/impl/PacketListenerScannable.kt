package kr.cosine.swamp.hunter.mod.core.common.scannable.impl

import io.github.classgraph.ClassInfo
import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet
import kr.cosine.swamp.hunter.mod.core.common.network.packet.definition.registry.PacketDefinitionRegistry
import kr.cosine.swamp.hunter.mod.core.common.network.packet.listener.PacketListener
import kr.cosine.swamp.hunter.mod.core.common.scannable.Scannable
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
internal abstract class PacketListenerScannable<C>(
    clazz: KClass<PacketListener<*, *>>,
    priority: Int = 100
) : Scannable<PacketListener<*, *>>(clazz, priority) {
    protected fun register(
        classInfo: ClassInfo,
        register: (CustomPacketPayload.Type<Packet>, (Packet, C) -> Unit) -> Unit
    ) {
        val packetListener = classInfo.loadClass(clazz).kotlin.objectInstance as? PacketListener<Packet, C> ?: return
        val packetClass = getGenericClass(packetListener)
        val packetDefinition = PacketDefinitionRegistry.find(packetClass) ?: return
        val type = packetDefinition.type as CustomPacketPayload.Type<Packet>
        register(type) { packet, context ->
            packetListener.handle(packet, context)
        }
    }

    private fun getGenericClass(packetListener: PacketListener<*, *>): KClass<*> {
        return packetListener::class
            .supertypes
            .first { it.isSubtypeOf(PacketListener::class.starProjectedType) }
            .arguments
            .first()
            .type!!
            .jvmErasure
    }
}