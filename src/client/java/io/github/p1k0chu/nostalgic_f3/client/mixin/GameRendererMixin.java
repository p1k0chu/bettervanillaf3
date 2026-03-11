package io.github.p1k0chu.nostalgic_f3.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.screens.debug.DebugOptionsScreen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(method = "render", at = @At(value = "NEW", target = "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/render/state/GuiRenderState;II)Lnet/minecraft/client/gui/GuiGraphics;"))
    GuiGraphics renderDebugBeforeGui(Minecraft minecraft, GuiRenderState guiRenderState, int i, int j, Operation<GuiGraphics> original) {
        var graphics = original.call(minecraft, guiRenderState, i, j);
        if (!(this.minecraft.screen instanceof DebugOptionsScreen)) {
            this.minecraft.gui.renderDebugOverlay(graphics);
        }
        return graphics;
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderDebugOverlay(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    void cancelOgDebugRender(Gui instance, GuiGraphics guiGraphics, Operation<Void> original) {
        // not calling original intentionally. too bad!
    }
}
