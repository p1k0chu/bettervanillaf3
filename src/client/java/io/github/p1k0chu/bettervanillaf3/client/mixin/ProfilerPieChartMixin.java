package io.github.p1k0chu.bettervanillaf3.client.mixin;

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
            method = "extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
    )
    private boolean hidePieChartBg(GuiGraphicsExtractor instance, int x0, int y0, int x1, int y1, int col) {
        return !BetterVanillaF3Config.getInstance().isDisabledPieChartBg();
    }

    @WrapMethod(method = "extractRenderState")
    private void scalePieChart(GuiGraphicsExtractor graphics, int scaledScreenWidth, int scaledScreenHeight, Operation<Void> original) {
        var scale = BetterVanillaF3Config.getInstance().getPieChartScale();
        graphics.pose().pushMatrix()
                .scale(scale);
        try {
            original.call(graphics, Math.round(scaledScreenWidth / scale), Math.round(scaledScreenHeight / scale));
        } finally {
            graphics.pose().popMatrix();
        }
    }

    @WrapOperation(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;profilerChart(Ljava/util/List;IIII)V"
            )
    )
    private void scalePiePosition(GuiGraphicsExtractor instance, List<ResultField> chartData, int x0, int y0, int x1, int y1, Operation<Void> original) {
        /*
         * Matrix scaling ruins the pie chart so we save the top matrix,
         * pop it, create the pie chart extractor, and push it back while
         * adjusting coordinates based on scale setting.
         */

        var scale = BetterVanillaF3Config.getInstance().getPieChartScale();
        float[] matrix = new float[3 * 2];
        instance.pose().get(matrix);
        instance.pose().popMatrix();
        try {
            original.call(
                    instance,
                    chartData,
                    (int) (x0 * scale),
                    (int) (y0 * scale),
                    (int) (x1 * scale),
                    (int) (y1 * scale)
            );
        } finally {
            instance.pose().pushMatrix().set(matrix);
        }
    }
}
