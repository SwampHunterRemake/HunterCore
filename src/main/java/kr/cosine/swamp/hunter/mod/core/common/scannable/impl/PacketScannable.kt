package kr.cosine.swamp.hunter.mod.core.common.scannable.impl

import io.github.classgraph.ClassInfo
import kr.cosine.swamp.hunter.mod.core.common.HunterCore
import kr.cosine.swamp.hunter.mod.core.common.network.packet.Packet
import kr.cosine.swamp.hunter.mod.core.common.network.packet.codec.Codec
import kr.cosine.swamp.hunter.mod.core.common.network.packet.codec.registry.CodecRegistry
import kr.cosine.swamp.hunter.mod.core.common.network.packet.definition.PacketDefinition
import kr.cosine.swamp.hunter.mod.core.common.network.packet.definition.registry.PacketDefinitionRegistry
import kr.cosine.swamp.hunter.mod.core.common.scannable.Scannable
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.modifier.Visibility
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodCall
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

internal object PacketScannable : Scannable<Packet>(Packet::class, 1) {
    private val Class<*>.path get() = simpleName
        .removeSuffix("${Packet::class.simpleName}\$codec")
        .replace(Regex("([A-Z]+)([A-Z][a-z])"), "$1_$2")
        .replace(Regex("([a-z0-9])([A-Z])"), "$1_$2")
        .lowercase()

    @Suppress("UNCHECKED_CAST")
    override fun handle(classInfo: ClassInfo) {
        val packetClass = classInfo.loadClass(clazz)
        val codecClass = ByteBuddy()
            .redefine(packetClass)
            .name(packetClass.name + "\$codec")
            .defineConstructor(Visibility.PUBLIC)
            .intercept(MethodCall.invokeSuper())
            .make()
            .load(packetClass.classLoader, ClassLoadingStrategy.Default.WRAPPER)
            .loaded

        val packetKotlinClass = packetClass.kotlin
        val codec = Codec(packetKotlinClass, codecClass)
        CodecRegistry.set(packetKotlinClass, codec)

        val identifier = Identifier.fromNamespaceAndPath(HunterCore.MOD_ID, packetClass.path)
        val type = CustomPacketPayload.Type<Packet>(identifier)
        val streamCodec = StreamCodec.of<RegistryFriendlyByteBuf, Packet>(
            { buf, packet ->
                packet.write(buf)
            },
            { buf ->
                val codec = CodecRegistry.find(packetKotlinClass)
                    ?: throw IllegalArgumentException("Packet '${packetKotlinClass.simpleName}' does not have a codec.")
                val codecClass = codec.codecClass
                val packetClassPrimaryConstructor = codec.packetClassPrimaryConstructor
                val codecPacket = codec.codecClassConstructor.newInstance()

                codecClass.getMethod("read", RegistryFriendlyByteBuf::class.java).invoke(codecPacket, buf)

                val params = mutableListOf<Any?>()
                packetClassPrimaryConstructor.parameters.forEach {
                    val name = it.name
                        ?: throw IllegalArgumentException("Packet '${packetKotlinClass.qualifiedName}' has a constructor parameter without a name.")
                    val field = codecClass.getDeclaredField(name)
                    field.isAccessible = true
                    params.add(field.get(codecPacket))
                    field.isAccessible = false
                }

                packetClassPrimaryConstructor.call(*params.toTypedArray())
            }
        )
        val packetDefinition = PacketDefinition(identifier, type, streamCodec)
        PacketDefinitionRegistry.set(packetKotlinClass, packetDefinition)
        PayloadTypeRegistry.clientboundPlay().register(type, streamCodec)
        // 서버 바운드도 있어야 함
    }
}