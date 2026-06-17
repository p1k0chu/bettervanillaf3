package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.DebugOptionsScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >=26.2 {
/*import net.minecraft.client.gui.Hud;
*///? } else
import net.minecraft.client.renderer.GameRenderer;

//? >=26.2 {
/*@Mixin(Gui.class)
*///? } else
@Mixin(GameRenderer.class)
class DebugOverlayMover_GuiMixin {
    //? >=26.2 {
    /*@Shadow
    @Final
    public Hud hud;

    @Shadow
    private Screen screen;
    *///? } else {
    @Shadow
    @Final
    private Minecraft minecraft;
    //? }

    //? >=26.2 {
    /*@Definition(id = "overlay", field = "Lnet/minecraft/client/gui/Gui;overlay:Lnet/minecraft/client/gui/screens/Overlay;")
    @Expression("this.overlay != null")
    @Inject(method = "extractRenderState", at = @At("MIXINEXTRAS:EXPRESSION"))
    *///? } else
    @Inject(method = /*$ extractGuiStr >> ','*/"extractGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", ordinal = 0))
    private void renderDebugBeforeGui(
            DeltaTracker deltaTracker,
            boolean shouldRenderLevel,
            /*? >=26.1 */boolean resourcesLoaded,
            CallbackInfo ci,
            @Local/*? >=26.1 >>+ ')'*/(name = "graphics") GuiGraphicsExtractor graphics
    ) {
        if (!(this. /*? >=26.2 { *//*screen*//*? } else >>*/minecraft.screen instanceof DebugOptionsScreen)) {
            //? >=26.2 {
            /*this.hud.extractDebugOverlay(graphics);
            *///? } else >=26.1 {
            this.minecraft.gui.extractDebugOverlay(graphics);
            //? } else
            //this.minecraft.gui.renderDebugOverlay(graphics);
        }
    }

    //? >=26.2 {
    /*@WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractDebugOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"))
    private void cancelOgDebugRender(Hud instance, GuiGraphicsExtractor graphics, Operation<Void> original) {
    }
    *///? } else {
    @WrapOperation(
            method = /*$ extractGuiStr >> ','*/"extractGui",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;" + /*$ extractDebugOverlayStr >> '+'*/"extractDebugOverlay"+ "(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
            )
    )
    private void cancelOgDebugRender(Gui instance, GuiGraphicsExtractor graphics, Operation<Void> original) {
    }
    //? }
}
