package kr.cosine.swamp.hunter.mod.core.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier

class HunterCoreClient : ClientModInitializer {
    override fun onInitializeClient() {
        val CATEGORY: KeyMapping.Category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("huntercore", "key")
        )

        val OPEN_VIEW = KeyMapping(
            "key.hunter.open_view", // The translation key for the key mapping.
            InputConstants.Type.KEYBOARD, // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
            InputConstants.KEY_V, // The keycode of the key.
            CATEGORY // The category of the mapping.
        );
        KeyMappingHelper.registerKeyMapping(OPEN_VIEW)
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (OPEN_VIEW.consumeClick()) {
                /*object : View(6, Component.literal("테스트")) {
                    override fun onClose(player: ServerPlayer) {
                        player.sendSystemMessage(Component.literal("인벤토리 닫힘"))
                    }
                }.open()*/
            }
        }
    }
}
