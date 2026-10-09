package kr.cosine.swamp.hunter.mod.core.server.view

import kr.cosine.swamp.hunter.mod.core.server.view.button.Button
import kr.cosine.swamp.hunter.mod.core.server.view.event.ButtonClickEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewClickEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewCreateEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewDestroyEvent
import kr.cosine.swamp.hunter.mod.core.server.view.event.ViewOpenEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

@Environment(EnvType.SERVER)
abstract class View(
    private val row: Int,
    private val title: Component,
    private val isCancelled: Boolean = true
) {
    val size = row * 9

    private val buttons = HashMap<Int, Button>()

    fun setButton(slot: Int, button: Button) {
        if (buttons[slot] == button) return
        if (slot >= size) throw IndexOutOfBoundsException(slot)
        buttons[slot] = button
    }

    open fun onCreate(event: ViewCreateEvent) {}

    open fun onOpen(event: ViewOpenEvent) {}

    open fun onDestroy(event: ViewDestroyEvent) {}

    open fun onClick(event: ViewClickEvent) {}

    fun open(viewer: ServerPlayer) {
        val viewCreateEvent = ViewCreateEvent(viewer)
        onCreate(viewCreateEvent)

        val container = SimpleContainer(size)
        buttons.forEach { (slot, button) ->
            if (slot in 0 until size) {
                container.setItem(slot, button.itemStack.copy())
            }
        }

        val menuType = getMenuTypeForSize()
        viewer.openMenu(
            object : MenuProvider {
                override fun getDisplayName(): Component {
                    return title
                }

                override fun createMenu(containerId: Int, inventory: Inventory, player: Player): AbstractContainerMenu {
                    return object : ChestMenu(menuType, containerId, inventory, container, row) {
                        override fun clicked(
                            slotIndex: Int,
                            buttonNum: Int,
                            containerInput: ContainerInput,
                            player: Player
                        ) {
                            val button = buttons[slotIndex]
                            if (button != null) {
                                val event = ButtonClickEvent(viewer, slotIndex, containerInput, buttonNum)
                                button.onClick(event)
                                sendAllDataToRemote()
                                return
                            }
                            val itemStack = if (0 <= slotIndex && slotIndex < slots.size) {
                                getSlot(slotIndex).item
                            } else {
                                ItemStack.EMPTY
                            }
                            val viewClickEvent = ViewClickEvent(viewer, slotIndex, itemStack, containerInput, buttonNum)
                            onClick(viewClickEvent)
                            if (isCancelled || viewClickEvent.isCancelled) {
                                sendAllDataToRemote()
                                return
                            }
                            super.clicked(slotIndex, buttonNum, containerInput, player)
                        }

                        override fun removed(player: Player) {
                            super.removed(player)
                            val viewDestroyEvent = ViewDestroyEvent(viewer)
                            onDestroy(viewDestroyEvent)
                        }
                    }
                }
            }
        )

        val viewOpenEvent = ViewOpenEvent(viewer)
        onOpen(viewOpenEvent)
    }

    private fun getMenuTypeForSize(): MenuType<ChestMenu> {
        return when (row) {
            1 -> MenuType.GENERIC_9x1
            2 -> MenuType.GENERIC_9x2
            3 -> MenuType.GENERIC_9x3
            4 -> MenuType.GENERIC_9x4
            5 -> MenuType.GENERIC_9x5
            6 -> MenuType.GENERIC_9x6
            else -> throw IllegalArgumentException("Invalid row $row")
        }
    }
}