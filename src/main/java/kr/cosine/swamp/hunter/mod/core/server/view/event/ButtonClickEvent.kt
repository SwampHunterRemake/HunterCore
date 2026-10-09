package kr.cosine.swamp.hunter.mod.core.server.view.event

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.ContainerInput

@Environment(EnvType.SERVER)
class ButtonClickEvent(
    val player: ServerPlayer,
    val slot: Int,
    containerInput: ContainerInput,
    buttonNum: Int
) : ClickEvent(containerInput, buttonNum)