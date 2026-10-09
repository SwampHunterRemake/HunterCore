package kr.cosine.swamp.hunter.mod.core.common.network.packet.listener

import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet

// 항상 data object 클래스로 상속
interface PacketListener<T : Packet, C> {
    fun handle(packet: T, context: C)
}