package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.p1k0chu.bettervanillaf3.client.BetterVanillaF3Config;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.debug.DebugOptionsScreen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = /*$ extractGuiStr >> ','*/"extractGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", ordinal = 0))
    private void renderDebugBeforeGui(
            DeltaTracker deltaTracker,
            boolean shouldRenderLevel,
            /*? >=26.1 */boolean resourcesLoaded,
            CallbackInfo ci,
            @Local/*? >=26.1 >>+ ')'*/(name = "graphics") GuiGraphicsExtractor graphics
    ) {
        if (!(this.minecraft.screen instanceof DebugOptionsScreen)) {
            if (BetterVanillaF3Config.getInstance().isHideOverlayWhenF1() && this.minecraft.options.hideGui) {
                return;
            }
            //? if <26.1 {
            /*this.minecraft.gui.renderDebugOverlay(graphics);
            *///? } else
            this.minecraft.gui.extractDebugOverlay(graphics);
        }
    }

    @WrapOperation(
            method = /*$ extractGuiStr >> ','*/"extractGui",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;" + /*$ extractDebugOverlayStr >> '+'*/"extractDebugOverlay"+ "(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
            )
    )
    private void cancelOgDebugRender(Gui instance, GuiGraphicsExtractor graphics, Operation<Void> original) {
        // not calling original intentionally. too bad!
    }
}
