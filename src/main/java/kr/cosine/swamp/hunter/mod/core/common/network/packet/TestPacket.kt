package kr.cosine.swamp.hunter.mod.core.common.network.packet

import net.minecraft.network.RegistryFriendlyByteBuf

data class TestPacket(
    var number: Int
) : Packet {
    override fun read(buf: RegistryFriendlyByteBuf) {
        number = buf.readInt()
    }

    override fun write(buf: RegistryFriendlyByteBuf) {
        buf.writeInt(number)
    }
}