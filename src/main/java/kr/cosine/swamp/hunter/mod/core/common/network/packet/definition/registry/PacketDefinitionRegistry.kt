package kr.cosine.swamp.hunter.mod.core.common.network.packet.definition.registry

import kr.cosine.swamp.hunter.mod.core.common.network.packet.definition.PacketDefinition
import kotlin.reflect.KClass

object PacketDefinitionRegistry {
    private val packetDefinitionMap = mutableMapOf<String, PacketDefinition<*>>()

    fun find(className: String): PacketDefinition<*>? {
        return packetDefinitionMap[className]
    }

    fun find(clazz: KClass<*>): PacketDefinition<*>? {
        return find(clazz.qualifiedName!!)
    }

    fun get(className: String): PacketDefinition<*> {
        return find(className) ?: throw IllegalArgumentException()
    }

    fun get(clazz: KClass<*>): PacketDefinition<*> {
        return find(clazz.qualifiedName!!) ?: throw IllegalArgumentException()
    }

    fun set(className: String, packetDefinition: PacketDefinition<*>) {
        packetDefinitionMap[className] = packetDefinition
    }

    fun set(clazz: KClass<*>, packetDefinition: PacketDefinition<*>) {
        set(clazz.qualifiedName!!, packetDefinition)
    }
}