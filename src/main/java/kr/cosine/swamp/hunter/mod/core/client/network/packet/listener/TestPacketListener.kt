package kr.cosine.swamp.hunter.mod.core.client.network.packet.listener

import kr.cosine.swamp.hunter.mod.core.common.network.packet.TestPacket
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
data object TestPacketListener : ClientPacketListener<TestPacket> {
    override fun handle(packet: TestPacket, context: ClientPlayNetworking.Context) {
        context.player().sendSystemMessage(Component.literal("숫자: ${packet.number}"))
    }
}