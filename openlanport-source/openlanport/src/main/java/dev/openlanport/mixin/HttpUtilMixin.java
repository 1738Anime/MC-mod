package dev.openlanport.mixin;

import dev.openlanport.PortHolder;
import net.minecraft.util.HttpUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * In 1.6.4, IntegratedServer#shareToLan asks HttpUtil for a random free port.
 * If the player chose one, return it instead.
 */
@Mixin(HttpUtil.class)
public class HttpUtilMixin {

    @Inject(method = "getFreePort", at = @At("HEAD"), cancellable = true)
    private static void openlanport$usePort(CallbackInfoReturnable<Integer> cir) {
        int port = PortHolder.consume();
        if (port > 0) {
            cir.setReturnValue(port);
        }
    }
}
