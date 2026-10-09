package kr.cosine.swamp.hunter.mod.core.server.scannable.impl

import io.github.classgraph.ClassInfo
import kr.cosine.swamp.hunter.mod.core.common.network.packet.listener.PacketListener
import kr.cosine.swamp.hunter.mod.core.common.scannable.impl.PacketListenerScannable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Environment(EnvType.SERVER)
internal object ServerPacketListenerScannable : PacketListenerScannable<ServerPlayNetworking.Context>(PacketListener::class) {
    override fun handle(classInfo: ClassInfo) {
        register(classInfo) { type, listener ->
            ServerPlayNetworking.registerGlobalReceiver(type, listener)
        }
    }
}