package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.p1k0chu.bettervanillaf3.client.BetterVanillaF3Config;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.util.profiling.ResultField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ProfilerPieChart.class)
class ProfilerPieChartMixin {
    @WrapWithCondition(
            method = /*$ extractRenderStateStr >> ',' */"extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
    )
    private boolean hidePieChartBg(GuiGraphicsExtractor instance, int x0, int y0, int x1, int y1, int col) {
        return !BetterVanillaF3Config.getInstance().isDisabledPieChartBg();
    }

    /*
     * Scale the Pie Chart
     *
     */

    @WrapMethod(method = /*$ extractRenderStateStr >> ')' */"extractRenderState")
    private void scalePieChartText(GuiGraphicsExtractor graphics, Operation<Void> original) {
        graphics.pose().pushMatrix()
                .scale(BetterVanillaF3Config.getInstance().getPieChartScale());
        try {
            original.call(graphics);
        } finally {
            graphics.pose().popMatrix();
        }
    }

    /*
     * Text's position is scaled too, so the gui size is scaled
     * backwards
     */

    @ModifyExpressionValue(
            method = /*$ extractRenderStateStr >> ',' */"extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiWidth()I")
    )
    private int unscaleGuiWidth(int original) {
        return Math.round(original / BetterVanillaF3Config.getInstance().getPieChartScale());
    }

    @ModifyExpressionValue(
            method = /*$ extractRenderStateStr >> ',' */"extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiHeight()I")
    )
    private int unscaleGuiHeight(int original) {
        return Math.round(original / BetterVanillaF3Config.getInstance().getPieChartScale());
    }

    /*
     * Matrix scaling doesn't affect the pie chart. It needs scaling
     * just because guiWidth and height was changed
     */
     
    @WrapOperation(
            method = /*$ extractRenderStateStr >> ',' */"extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;" +
                    //? >=26.1 {
                            "profilerChart"
                    //? } else {
                            /*"submitProfilerChartRenderState"
                    *///? }
                            + "(Ljava/util/List;IIII)V"
            )
    )
    private void scalePiePosition(GuiGraphicsExtractor instance, List<ResultField> chartData, int x0, int y0, int x1, int y1, Operation<Void> original) {
        original.call(
                instance,
                chartData,
                (int) (x0 * BetterVanillaF3Config.getInstance().getPieChartScale()),
                (int) (y0 * BetterVanillaF3Config.getInstance().getPieChartScale()),
                (int) (x1 * BetterVanillaF3Config.getInstance().getPieChartScale()),
                (int) (y1 * BetterVanillaF3Config.getInstance().getPieChartScale())
        );
    }
}
