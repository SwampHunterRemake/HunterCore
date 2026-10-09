package kr.cosine.swamp.hunter.mod.core.client.network.packet.listener

import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet
import kr.cosine.swamp.hunter.mod.core.common.network.packet.listener.PacketListener
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Environment(EnvType.CLIENT)
interface ClientPacketListener<T : Packet> : PacketListener<T, ClientPlayNetworking.Context>