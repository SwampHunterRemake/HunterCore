package kr.cosine.swamp.hunter.mod.core.common.network.packet.definition

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class PacketDefinition<T : CustomPacketPayload>(
    val identifier: Identifier,
    val type: CustomPacketPayload.Type<T>,
    val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
)