package kr.cosine.swamp.hunter.mod.core.server.view.button

import kr.cosine.swamp.hunter.mod.core.server.view.View
import kr.cosine.swamp.hunter.mod.core.server.view.event.ButtonClickEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.world.item.ItemStack

@Environment(EnvType.SERVER)
data class Button(
    val itemStack: ItemStack,
    val onClick: (ButtonClickEvent) -> Unit
) {
    fun setSlot(view: View, slot: Int) {
        view.setButton(slot, this)
    }
}