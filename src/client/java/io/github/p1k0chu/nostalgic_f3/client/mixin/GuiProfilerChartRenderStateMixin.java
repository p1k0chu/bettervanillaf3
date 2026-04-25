package io.github.p1k0chu.nostalgic_f3.client.mixin;

import io.github.p1k0chu.nostalgic_f3.client.NostalgicF3Config;
//? >=26.1 {
import net.minecraft.client.renderer.state.gui.pip.GuiProfilerChartRenderState;
//? } else
//import net.minecraft.client.gui.render.state.pip.GuiProfilerChartRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiProfilerChartRenderState.class)
class GuiProfilerChartRenderStateMixin {
    @Inject(method = "scale", at = @At("RETURN"), cancellable = true)
    private void modifyScale(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(NostalgicF3Config.getInstance().getPieChartScale() * cir.getReturnValueF());
    }
}
