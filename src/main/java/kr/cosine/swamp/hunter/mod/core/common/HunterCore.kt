package kr.cosine.swamp.hunter.mod.core.common

import kr.cosine.swamp.hunter.mod.core.common.scannable.Scannable
import net.fabricmc.api.ModInitializer

class HunterCore : ModInitializer {
    override fun onInitialize() {
        Scannable.register()
    }

    internal companion object {
        const val MOD_ID = "huntercore"
    }
}
