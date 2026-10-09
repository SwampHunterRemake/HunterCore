package kr.cosine.swamp.hunter.mod.core.client.scannable.impl

import io.github.classgraph.ClassInfo
import kr.cosine.swamp.hunter.mod.core.common.network.packet.listener.PacketListener
import kr.cosine.swamp.hunter.mod.core.common.scannable.impl.PacketListenerScannable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Environment(EnvType.CLIENT)
internal object ClientPacketListenerScannable : PacketListenerScannable<ClientPlayNetworking.Context>(PacketListener::class) {
    override fun handle(classInfo: ClassInfo) {
        register(classInfo) { type, listener ->
            ClientPlayNetworking.registerGlobalReceiver(type, listener)
        }
    }
}