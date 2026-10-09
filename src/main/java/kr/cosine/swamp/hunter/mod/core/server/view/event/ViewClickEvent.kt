package kr.cosine.swamp.hunter.mod.core.server.view.event

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.item.ItemStack

@Environment(EnvType.SERVER)
class ViewClickEvent(
    val player: ServerPlayer,
    val slot: Int,
    val itemStack: ItemStack,
    containerInput: ContainerInput,
    buttonNum: Int
) : ClickEvent(containerInput, buttonNum) {
    var isCancelled = false
}