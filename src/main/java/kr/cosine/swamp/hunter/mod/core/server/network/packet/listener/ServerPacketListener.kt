package kr.cosine.swamp.hunter.mod.core.server.network.packet.listener

import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet
import kr.cosine.swamp.hunter.mod.core.common.network.packet.listener.PacketListener
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Environment(EnvType.SERVER)
interface ServerPacketListener<T : Packet> : PacketListener<T, ServerPlayNetworking.Context>