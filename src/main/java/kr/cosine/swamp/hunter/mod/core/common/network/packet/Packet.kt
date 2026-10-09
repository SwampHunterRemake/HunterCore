package kr.cosine.swamp.hunter.mod.core.common.network.packet

import kr.cosine.swamp.hunter.mod.core.common.network.packet.definition.registry.PacketDefinitionRegistry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface Packet : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return PacketDefinitionRegistry.get(this::class).type
    }

    fun read(buf: RegistryFriendlyByteBuf)

    fun write(buf: RegistryFriendlyByteBuf)
}