package kr.cosine.swamp.hunter.mod.core.server.view.event

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.server.level.ServerPlayer

@Environment(EnvType.SERVER)
data class ViewDestroyEvent(
    val player: ServerPlayer
)