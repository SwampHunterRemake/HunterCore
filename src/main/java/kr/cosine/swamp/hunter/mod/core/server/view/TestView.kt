package kr.cosine.swamp.hunter.mod.core.server.view

import kr.cosine.swamp.hunter.mod.core.server.view.button.Button
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewClickEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewCreateEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewDestroyEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewOpenEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Items

@Environment(EnvType.SERVER)
class TestView : View(6, Component.literal("테스트"), false) {
    override fun onCreate(event: ViewCreateEvent) {
        Button(Items.DIAMOND.defaultInstance) { event ->
            if (event.isShiftLeftClick) {
                event.player.sendSystemMessage(Component.literal("쉬프트 좌클릭"))
                return@Button
            }
            if (event.isShiftRightClick) {
                event.player.sendSystemMessage(Component.literal("쉬프트 우클릭"))
                return@Button
            }
            if (event.isLeftClick) {
                event.player.sendSystemMessage(Component.literal("좌클릭"))
                return@Button
            }
            if (event.isRightClick) {
                event.player.sendSystemMessage(Component.literal("우클릭"))
                return@Button
            }
            if (event.isMiddleClick) {
                event.player.sendSystemMessage(Component.literal("휠클릭"))
                return@Button
            }
        }.setSlot(this, 0)
    }

    override fun onOpen(event: ViewOpenEvent) {
        event.player.sendSystemMessage(Component.literal("열림"))
    }

    override fun onClick(event: ViewClickEvent) {
        if (event.itemStack.`is`(Items.COW_SPAWN_EGG)) {
            event.isCancelled = true
            event.player.sendSystemMessage(Component.literal("캔슬"))
        }
    }

    override fun onDestroy(event: ViewDestroyEvent) {
        event.player.sendSystemMessage(Component.literal("닫힘"))
    }
}