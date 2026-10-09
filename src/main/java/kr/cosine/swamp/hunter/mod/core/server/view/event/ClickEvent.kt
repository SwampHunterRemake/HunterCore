package kr.cosine.swamp.hunter.mod.core.server.view.event

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.world.inventory.ContainerInput

@Environment(EnvType.SERVER)
abstract class ClickEvent(
    private val containerInput: ContainerInput,
    private val buttonNum: Int,
) {
    val isLeftClick: Boolean get() = containerInput == ContainerInput.PICKUP && buttonNum == 0
    val isRightClick: Boolean get() = containerInput == ContainerInput.PICKUP && buttonNum == 1
    val isMiddleClick: Boolean get() = buttonNum == 2

    val isShiftLeftClick: Boolean get() = containerInput == ContainerInput.QUICK_MOVE && buttonNum == 0
    val isShiftRightClick: Boolean get() = containerInput == ContainerInput.QUICK_MOVE && buttonNum == 1
}