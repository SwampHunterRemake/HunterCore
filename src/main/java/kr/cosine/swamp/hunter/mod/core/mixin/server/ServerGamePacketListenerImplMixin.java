package kr.cosine.swamp.hunter.mod.core.mixin.server;

import kr.cosine.swamp.hunter.mod.core.common.network.packet.TestPacket;
import kr.cosine.swamp.hunter.mod.core.server.view.TestView;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 1, shift = At.Shift.BEFORE), cancellable = true)
    public void onPlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo callbackInfo) {
        System.out.println("!!!!!!!!!!!!!!!! 양손 클릭");
        new TestView().open(player);
        ServerPlayNetworking.send(player, new TestPacket(ThreadLocalRandom.current().nextInt(1, 100)));
        callbackInfo.cancel();
    }
}
